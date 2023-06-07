package com.eewoo.chat.service.impl;


import com.eewoo.chat.service.ChatService;
import com.eewoo.chat.pojo.Chat;
import com.eewoo.common.pojo.vo.request.CounselorCommentRequest;
import com.eewoo.common.pojo.vo.request.SessionRequest;
import com.eewoo.common.pojo.vo.request.VisitorCommentRequest;
import com.eewoo.common.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.eewoo.chat.feign.PlatformFeign;
import com.eewoo.chat.pojo.ChatInfo;
import com.eewoo.chat.pojo.CounselorComment;
import com.eewoo.chat.pojo.SR;
import com.eewoo.chat.pojo.VisitorComment;
import com.eewoo.chat.service.ChatScheduler;
import com.eewoo.chat.socket.WebSocketServer;
import com.eewoo.common.pojo.Supervisor;
import com.eewoo.common.pojo.User;
import com.eewoo.common.security.LoginUser;
import com.eewoo.common.util.Constant;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.websocket.Session;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@Service
public class ChatServiceImpl implements ChatService {
    @Autowired

    private MongoTemplate mongoTemplate;

    @Override
    public Chat findById(Long id) {
        Query query=new Query();
        Criteria criteria=Criteria.where("id").is(id);
        query.addCriteria(criteria);
        return mongoTemplate.findOne(query,Chat.class);
    }


    @Autowired
    PlatformFeign platformFeign;

    static Random random = new Random();
    String getToken(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("token");
    } //获取的header的jwt

    public boolean callCounselor(Integer counselorId){
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

        makeVCSession(visitorKey, counselorKey); //加入会话
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
        makeVCSession(visitorKey, chatInfo.getSenderKey());
    }

    @Override
    public boolean callSupervisor(String chatToken) {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer counselorId = user.getId();
        String counselorKey = "c:" + counselorId;

        //TODO platformFeign.getSupervisor();
        Supervisor supervisor = new Supervisor();
        String supervisorKey = "s:" + supervisor.getId();

        if (!WebSocketServer.sessionMap.containsKey(supervisorKey))
            return false;

        Session supervisorSession = WebSocketServer.sessionMap.get(supervisorKey);
        Session counselorSession = WebSocketServer.sessionMap.get(counselorKey);
        String counselorChatToken = genChatToken(counselorKey, supervisorKey, null);
        String supervisorChatToken = genChatToken(supervisorKey, counselorKey, null);
        WebSocketServer.sendMessage(SR.chatToken(counselorChatToken, supervisorKey), supervisorSession);
        WebSocketServer.sendMessage(SR.chatToken(supervisorChatToken, counselorKey), counselorSession);
        WebSocketServer.chatMap.put(counselorKey + "->" + supervisorKey, counselorChatToken);
        WebSocketServer.chatMap.put(supervisorKey + "->" + counselorKey, supervisorChatToken);

        return true;
    }

    @Override
    public void endCSSession(String senderChatToken) {
        //主动发起结束一方的信息
        ChatInfo senderChatInfo = WebSocketServer.tokenInfoMap.get(senderChatToken);

        //移除chatToken
        String senderKey = senderChatInfo.getSenderKey();
        String receiverKey = senderChatInfo.getReceiverKey();

        //移除chatMap
        WebSocketServer.chatMap.remove(senderKey+"->"+receiverKey);
        WebSocketServer.chatMap.remove(receiverKey+"->"+senderKey);
        //移除chatTokenMap
        WebSocketServer.tokenInfoMap.remove(senderKey);
        WebSocketServer.tokenInfoMap.remove(receiverKey);
    }


    /**
     * 生成临时会话token
     * @param senderKey
     * @param receiverKey
     * @param sessionId
     * @return 返回chatToken
     */
    private String genChatToken(String senderKey, String receiverKey, Integer sessionId){
        String uuid = UUID.randomUUID().toString().replace("-", "");
        ChatInfo chatInfo = new ChatInfo(true, senderKey, receiverKey, sessionId);
        WebSocketServer.tokenInfoMap.put(uuid, chatInfo);
        return uuid;
    }

    /**
     * 使等待的访客加入会话
     * @param visitorKey
     * @param counselorKey
     */
    private void makeVCSession(String visitorKey, String counselorKey){
        SessionRequest sessionRequest = new SessionRequest();
        sessionRequest.setCounselorId(Integer.parseInt(counselorKey.substring(2)));
        sessionRequest.setVisitorId(Integer.parseInt(visitorKey.substring(2)));
        sessionRequest.setStartTime(new Date());
        R r = platformFeign.createSession(sessionRequest, getToken());
        if (r==null) return;
        Integer sessionId = (Integer) r.getData();// 可以正式发起新的会话，得到的sessionId
        String visitorChatToken = genChatToken(visitorKey, counselorKey, sessionId);
        String counselorChatToken = genChatToken(counselorKey, visitorKey, sessionId);

        //通过webSocket将token发给各自，告诉访客和哪个咨询师的chatToken已经有了，告诉咨询师和哪个访客的chatToken已经有了
        WebSocketServer.sendMessage(SR.chatToken(visitorChatToken, counselorKey), WebSocketServer.sessionMap.get(visitorKey));
        WebSocketServer.sendMessage(SR.chatToken(counselorChatToken, visitorKey), WebSocketServer.sessionMap.get(counselorKey));
        //二者之间建立会话
        WebSocketServer.chatMap.put(visitorKey+"->"+counselorKey, visitorChatToken);
        WebSocketServer.chatMap.put(counselorKey+"->"+visitorKey, counselorChatToken);

        //时间戳开始计时
        ChatScheduler.updateTime(visitorChatToken);
        ChatScheduler.updateTime(counselorChatToken);
    }


}
