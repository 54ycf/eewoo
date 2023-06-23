package com.eewoo.chat.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.eewoo.chat.pojo.*;
import com.eewoo.chat.service.ChatService;
import com.eewoo.chat.service.StoreChatSCService;
import com.eewoo.chat.service.StoreChatService;
import com.eewoo.common.pojo.vo.request.CounselorCommentRequest;
import com.eewoo.common.pojo.vo.request.SessionSCRequest;
import com.eewoo.common.pojo.vo.request.SessionRequest;
import com.eewoo.common.pojo.vo.request.VisitorCommentRequest;
import com.eewoo.common.util.R;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eewoo.chat.feign.PlatformFeign;
import com.eewoo.chat.service.ChatScheduler;
import com.eewoo.chat.socket.WebSocketServer;
import com.eewoo.common.pojo.Supervisor;
import com.eewoo.common.pojo.User;
import com.eewoo.common.security.LoginUser;
import com.eewoo.common.util.Constant;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    PlatformFeign platformFeign;

    @Autowired
    StoreChatService storeChatService;
    @Autowired
    StoreChatSCService storeChatSCService;

    String getToken(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("token");
    } //获取的header的jwt

    public boolean callCounselor(Integer counselorId, String counselorName){
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer visitorId = user.getId();
        String visitorKey = "v:" + visitorId;
        String counselorKey = "c:" + counselorId;

        if (WebSocketServer.chatMap.containsKey(visitorKey+"->"+counselorKey))
            return false; //说明已经有了这二者之间的会话，不能再次发起会话

        List<String> waitingVisitors = WebSocketServer.waitingListMap.get(counselorKey);
        if (waitingVisitors == null)
            return false;

        waitingVisitors.add(visitorKey); //加入队列
        if (waitingVisitors.size() > Constant.MAX_CHAT) //排队人数超标，等待 // TODO 感觉这地方的并发访问一致性控制不够
            return false;

        makeVCSession(visitorKey, user.getName(), counselorKey, counselorName); //加入会话
        return true;
    }

    @Override
    public void endVCSession(String senderChatToken) {
        //主动发起结束一方的信息
        ChatInfo senderChatInfo = WebSocketServer.tokenInfoMap.get(senderChatToken);

        //先让会话两者的token失效，不许再聊天
        senderChatInfo.setIsValid(false);
        String senderKey = senderChatInfo.getSenderKey();
        String receiverKey = senderChatInfo.getReceiverKey();
        String receiverChatToken = WebSocketServer.chatMap.get(receiverKey + "->" + senderKey);
        if (receiverChatToken == null)
            return; //说明要么对方已经先发起会话过了，或者对方会话超时导致的你已经不需要再断会话了，这属于并发问题
        ChatInfo receiverChatInfo = WebSocketServer.tokenInfoMap.get(receiverChatToken);
        receiverChatInfo.setIsValid(false);

        //移除chatMap
        WebSocketServer.chatMap.remove(senderKey+"->"+receiverKey);
        WebSocketServer.chatMap.remove(receiverKey+"->"+senderKey);

        //存储进mongo
        Chat chat = WebSocketServer.sessionIdChatMap.get(senderChatInfo.getSessionId());
        storeChatService.save(chat);
        WebSocketServer.sessionIdChatMap.remove(senderChatInfo.getSessionId()); //聊天记录从内存删除

        //通知两方会话已结束，请进行评价
        WebSocketServer.sendMessage(SR.commentNotify(senderChatToken), WebSocketServer.sessionMap.get(senderKey));
        WebSocketServer.sendMessage(SR.commentNotify(receiverChatToken), WebSocketServer.sessionMap.get(receiverKey));

        //移除chatToken，不再对此计时
        ChatScheduler.removeChatToken(senderChatToken);
        ChatScheduler.removeChatToken(receiverChatToken);
    }


    @Override
    public void visitorComment(VisitorComment visitorComment) {
        String chatToken = visitorComment.getChatToken();
        ChatInfo chatInfo = WebSocketServer.tokenInfoMap.get(chatToken);
        VisitorCommentRequest visitorCommentRequest = new VisitorCommentRequest();
        visitorCommentRequest.setSessionId(chatInfo.getSessionId());
        visitorCommentRequest.setVisitorFeedback(visitorComment.getVisitorFeedback());
        visitorCommentRequest.setVisitorFeedbackScore(visitorComment.getVisitorFeedbackScore());
        platformFeign.giveCounselorComment(visitorCommentRequest, getToken());
        //移除访客的chatToken和相关信息
        WebSocketServer.tokenInfoMap.remove(chatToken);
    }

    @Override
    public void counselorComment(CounselorComment counselorComment) {
        String chatToken = counselorComment.getChatToken();
        System.out.println(chatToken);
        WebSocketServer.tokenInfoMap.forEach((k,v) -> System.out.println(k+" " + v.getSenderName() +" "+ v.getReceiverName()));
        ChatInfo chatInfo = WebSocketServer.tokenInfoMap.get(chatToken);
        CounselorCommentRequest counselorCommentRequest = new CounselorCommentRequest();
        counselorCommentRequest.setSessionId(chatInfo.getSessionId());
        counselorCommentRequest.setFeedback(counselorComment.getCounselorFeedback());
        counselorCommentRequest.setType(counselorComment.getType());
        platformFeign.giveVisitorComment(counselorCommentRequest, getToken());

        //清除咨询师的chatToken及其相关信息
        WebSocketServer.tokenInfoMap.remove(chatToken);

        //用户访问完成，用队列里剔除
        List<String> waitingVisitors = WebSocketServer.waitingListMap.get(chatInfo.getSenderKey());
        waitingVisitors.remove(chatInfo.getReceiverKey()); //receiver是访客

        if (waitingVisitors.size() <= Constant.MAX_CHAT) {
            return; //没有人排队，直接结束
        }
        //v:1 将下一个排队成功访客加入进来
        String visitorKey = waitingVisitors.get(Constant.MAX_CHAT);
        makeVCSession(visitorKey, chatInfo.getReceiverName(), chatInfo.getSenderKey(), chatInfo.getSenderName());
    }

    @Override
    public boolean callSupervisor(String chatToken) {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer counselorId = user.getId();
        String counselorKey = "c:" + counselorId;
        String counselorName = user.getUsername();

        //TODO platformFeign.getSupervisor();
        R<Supervisor> r0 = platformFeign.getSupervisor(getToken());
        if (r0==null) return false;
        Supervisor supervisor = r0.getData();
        String supervisorKey = "s:" + supervisor.getId();
        String supervisorName = supervisor.getUsername();

        if (!WebSocketServer.sessionMap.containsKey(supervisorKey))
            return false;

        SessionSCRequest sessionRequest = new SessionSCRequest();
        sessionRequest.setCounselorId(Integer.parseInt(counselorKey.substring(2)));
        sessionRequest.setSupervisorId(Integer.parseInt(supervisorKey.substring(2)));
        sessionRequest.setStartTime(new Date());
        R<Integer> r1 = platformFeign.createSCSession(sessionRequest, getToken());//TODO
        if (r1==null) return false;
        Integer sessionId = (Integer) r1.getData();// 可以正式发起新的会话，得到的SC之间的sessionId
        String counselorChatToken = genChatToken(counselorKey, counselorName, supervisorKey, supervisorName, sessionId);
        String supervisorChatToken = genChatToken(supervisorKey, supervisorName, counselorKey, counselorName, sessionId);

        //聊天记录内存暂存
        ChatSC chatSC = new ChatSC();
        chatSC.setId(Long.valueOf(sessionId));
        chatSC.setParticipants(Lists.newArrayList(supervisorName, counselorName));
        WebSocketServer.sessionIdChatSCMap.put(sessionId, chatSC);

        ChatInfo chatInfo = WebSocketServer.tokenInfoMap.get(chatToken);
        Integer baseSessionId = chatInfo.getSessionId();

        //通过webSocket将token发给各自，告诉访客和哪个咨询师的chatToken已经有了，告诉咨询师和哪个访客的chatToken已经有了
        WebSocketServer.sendMessage(SR.chatTokenSC(supervisorChatToken, counselorKey, counselorName, baseSessionId, sessionId), WebSocketServer.sessionMap.get(supervisorKey));
        WebSocketServer.sendMessage(SR.chatTokenSC(counselorChatToken, supervisorKey, supervisorName, baseSessionId, sessionId), WebSocketServer.sessionMap.get(counselorKey));

        //转发映射
        WebSocketServer.forwardMap.put(baseSessionId, WebSocketServer.sessionMap.get(supervisorKey));
        WebSocketServer.ididMap.put(baseSessionId, sessionId);

        Chat chat = WebSocketServer.sessionIdChatMap.get(baseSessionId);
        if (chat != null) { //把访客-咨询师之间的此次的聊天转发过来先
            WebSocketServer.sendMessage(SR.msgInit(chat.getMessages(), sessionId), WebSocketServer.sessionMap.get(supervisorKey));
        }

        return true;
    }

    @Override
    public void endSCSession(String senderChatToken) {
        //主动发起结束一方的信息
        ChatInfo senderChatInfo = WebSocketServer.tokenInfoMap.get(senderChatToken);

        Integer key = null;
        for (Map.Entry<Integer, Integer> integerIntegerEntry : WebSocketServer.ididMap.entrySet()) {
            key = integerIntegerEntry.getKey();
            Integer value = integerIntegerEntry.getValue();
            if (value.equals(senderChatInfo.getSessionId())) {
                break;
            }
        }
        if (key!=null){
            WebSocketServer.ididMap.remove(key);
            WebSocketServer.sessionIdChatSCMap.remove(key);
        }

        ChatSC chatSC = WebSocketServer.sessionIdChatSCMap.get(senderChatInfo.getSessionId());
        storeChatSCService.save(chatSC);
        WebSocketServer.sessionIdChatSCMap.remove(senderChatInfo.getSessionId()); //聊天记录从内存删除

        platformFeign.endSCSession(senderChatInfo.getSessionId(), getToken());
    }

    @Override
    public Integer getChatsNum() {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        List<String> waitingVisitors = WebSocketServer.waitingListMap.get("c:" + user.getId());
        if (waitingVisitors == null) return 0;
        return waitingVisitors.size();
    }

    @Override
    public List<Integer> getOnlineCounselors() {
        List<Integer> result = new ArrayList<>();
        for (String s : WebSocketServer.sessionMap.keySet()) {
            if (s.startsWith("c:")){
                String substring = s.substring(2);
                result.add(Integer.valueOf(substring));
            }
        }
        return result;
    }

    @SneakyThrows
    @Override
    public void getSessionInMongo(Integer sessionId, HttpServletResponse response) {
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("session-"+sessionId+".txt", "UTF-8"));
        response.setContentType("application/octet-stream");
        Chat chat = storeChatService.findById(sessionId);
        byte[] data = JSON.toJSONBytes(chat, SerializerFeature.PrettyFormat);
        ServletOutputStream os;
        os = response.getOutputStream();
        os.write(data);
        os.flush();
        os.close();
    }

    @SneakyThrows
    @Override
    public void getSessionsInMongo(List<Integer> sessionIds, HttpServletResponse response) {
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("sessions.zip", "UTF-8"));
        response.setContentType("application/octet-stream");

        List<ZipItem> zipItems = new ArrayList<>();
        for (Integer sessionId : sessionIds) {
            Chat chat = storeChatService.findById(1);
            byte[] data = JSON.toJSONBytes(chat, SerializerFeature.PrettyFormat);
            zipItems.add(new ZipItem(data, sessionId));
        }
        byte[] zipData = zip(zipItems);

        ServletOutputStream os;
        os = response.getOutputStream();
        os.write(zipData);
        os.flush();
        os.close();
    }

    @Override
    public Chat getSessionContent(Integer sessionId) {
        return storeChatService.findById(sessionId);
    }


    /**
     * 生成临时会话token
     * @param senderKey
     * @param receiverKey
     * @param sessionId
     * @return 返回chatToken
     */
    private String genChatToken(String senderKey, String senderName, String receiverKey, String receiverName, Integer sessionId){
        String uuid = UUID.randomUUID().toString().replace("-", "");
        ChatInfo chatInfo = new ChatInfo(true, senderKey, senderName, receiverKey, receiverName, sessionId);
        WebSocketServer.tokenInfoMap.put(uuid, chatInfo);
        return uuid;
    }

    /**
     * 使等待的访客加入会话
     * @param visitorKey
     * @param counselorKey
     */
    private void makeVCSession(String visitorKey, String visitorName, String counselorKey, String counselorName){
        SessionRequest sessionRequest = new SessionRequest();
        sessionRequest.setCounselorId(Integer.parseInt(counselorKey.substring(2)));
        sessionRequest.setVisitorId(Integer.parseInt(visitorKey.substring(2)));
        sessionRequest.setStartTime(new Date());
        R r = platformFeign.createSession(sessionRequest, getToken());
        if (r==null) return;
        Integer sessionId = (Integer) r.getData();// 可以正式发起新的会话，得到的sessionId
        String visitorChatToken = genChatToken(visitorKey, visitorName, counselorKey, counselorName, sessionId);
        String counselorChatToken = genChatToken(counselorKey, counselorName, visitorKey, visitorName, sessionId);

        //聊天记录内存暂存
        Chat chat = new Chat();
        chat.setId(Long.valueOf(sessionId));
        chat.setParticipants(Lists.newArrayList(visitorName, counselorName));
        WebSocketServer.sessionIdChatMap.put(sessionId, chat);

        //通过webSocket将token发给各自，告诉访客和哪个咨询师的chatToken已经有了，告诉咨询师和哪个访客的chatToken已经有了
        WebSocketServer.sendMessage(SR.chatToken(visitorChatToken, counselorKey, counselorName, sessionId), WebSocketServer.sessionMap.get(visitorKey));
        WebSocketServer.sendMessage(SR.chatToken(counselorChatToken, visitorKey, visitorName, sessionId), WebSocketServer.sessionMap.get(counselorKey));
        //二者之间建立会话
        WebSocketServer.chatMap.put(visitorKey+"->"+counselorKey, visitorChatToken);
        WebSocketServer.chatMap.put(counselorKey+"->"+visitorKey, counselorChatToken);

        //时间戳开始计时
        ChatScheduler.updateTime(visitorChatToken);
        ChatScheduler.updateTime(counselorChatToken);
    }


    /**
     * 压缩多个txt
     * @param zipItems
     * @return
     */
    @SneakyThrows
    private byte[] zip(List<ZipItem> zipItems){
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zipOut = new ZipOutputStream(baos)) {
            for (ZipItem zipItem : zipItems) {
                byte[] txtFileBytes = zipItem.getTxtFileBytes();
                ZipEntry entry1 = new ZipEntry("session-"+zipItem.getSessionId()+".txt");
                zipOut.putNextEntry(entry1);
                zipOut.write(txtFileBytes);
                zipOut.closeEntry();
            }
            zipOut.finish();
            zipOut.close();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Data
    @AllArgsConstructor
    static class ZipItem{
        byte[] txtFileBytes;
        Integer sessionId;
    }
}
