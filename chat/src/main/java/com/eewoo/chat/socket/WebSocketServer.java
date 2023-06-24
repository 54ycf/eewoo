package com.eewoo.chat.socket;

import com.alibaba.fastjson.JSON;
import com.eewoo.chat.feign.AuthFeign;
import com.eewoo.chat.pojo.*;
import com.eewoo.chat.service.ChatScheduler;
import com.eewoo.common.pojo.Message;
import com.eewoo.common.security.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author websocket服务
 */
@ServerEndpoint(value = "/imserver/{token}")
@Component
public class WebSocketServer {
    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    static AuthFeign authFeign;//多线程的websocket，无法使用autowired直接注入
    //因为websocket是多实例单线程的，而websocket中的对象在@Autowried时，只有整个项目启动时会注入，而之后新的websocket实例都不会再次注入，故websocket上@Autowried的bean是会为null的
    @Autowired
    public void setAuthFeign(AuthFeign authFeign){WebSocketServer.authFeign = authFeign;}


    //记录当前在线连接，key为role:id
    public static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    //在线咨询师的等待列表，key c:id
    public static final Map<String, List<String>> waitingListMap = new ConcurrentHashMap<>();

    //记录chatToken -> 聊天信息
    public static final Map<String, ChatInfo> tokenInfoMap = new ConcurrentHashMap<>();

    // v:id->c:id 或者 c:id->s:id   -> chatToken
    public static final Map<String, String> chatMap = new ConcurrentHashMap<>();
    //记录访客咨询师的聊天 v:id->c:id 或者 c:id->s:id
//    public static final Set<String> chatSet = new ConcurrentHashSet<>();

    //sessionId -> chat
    public static final Map<Integer, Chat> sessionIdChatMap = new ConcurrentHashMap<>();
    //sessionId -> chatSC
    public static final Map<Integer, ChatSC> sessionIdChatSCMap = new ConcurrentHashMap<>();

    //用于转发 baseSessionId -> supervisor:Session
    public static final Map<Integer, Session> forwardMap = new ConcurrentHashMap<>();
    //baseSessionId -> sessionId
    public static final Map<Integer, Integer> ididMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     * 浏览器向服务端发送信息
     * @param session
     * @param token
     * @throws IOException */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        LoginUser loginUser = authFeign.parseToken(token).getData();
        if (null == loginUser){
            sendMessage(SR.err("token失效，连接失败"), session);
            session.close(); //我认为更好的方法是在建立websocket之前进行校验，每次建立连接再判断token非法再关闭连接是不必要的开销，但是就这样吧
            return;
        }

        Integer id = loginUser.getUser().getId();
        String role = loginUser.getUser().getRole();
        String key = role + ":" + id;
        sessionMap.put(key, session); //维护session
        if ("c".equals(role)){
            waitingListMap.put(key, Collections.synchronizedList(new ArrayList<>())); //咨询师上线，可以排队等待，注意List要加锁访问
        }

        sendMessage(SR.info("成功连接"), session);

    }

    /**
     * 连接关闭调用的方法 TODO close到底谁关闭触发的 */
    @OnClose
    public void onClose(Session session, @PathParam("token") String token) throws IOException {
        LoginUser loginUser = authFeign.parseToken(token).getData();
        if (null != loginUser){
            Integer id = loginUser.getUser().getId();
            String role = loginUser.getUser().getRole();
            String key = role + ":" + id;
            session.close();
            sessionMap.remove(key);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     * 后台收到客户端发送过来的消息
     * onMessage 是一个消息的中转站
     * 接受 浏览器端 socket.send 发送过来的 json数据
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("token") String token) {
        MessageSend msg = JSON.parseObject(message, MessageSend.class);
        String chatToken = msg.getChatToken();
        if(null == chatToken){
            sendMessage(SR.err("无临时会话token"), session); //告诉发送者没有成功
            return;
        }
        ChatInfo chatInfo = tokenInfoMap.get(chatToken);
        if(null == chatInfo || !chatInfo.getIsValid()){
            sendMessage(SR.err("临时会话token无效"), session); //告诉发送者没有成功
            return;
        }
        Session sendTo = sessionMap.get(chatInfo.getReceiverKey());
        if (null == sendTo){
            sendMessage(SR.err("对方已离线"), session); //告诉发送者没有成功
            return;
        }
        if (!chatInfo.getSenderKey().startsWith("s") && !chatInfo.getReceiverKey().startsWith("s")){ //只有访客-咨询师
            sendMessage(SR.msg(msg.getContent(), chatInfo.getSenderKey()), sendTo);
            ChatScheduler.updateTime(chatToken);//更新chatToken时间
            sessionIdChatMap.get(chatInfo.getSessionId()).getMessages().add(new Message(chatInfo.getSenderName(),chatInfo.getReceiverName(),msg.getContent(),new Date().toString()));//聊天记录先存在内存，加一条
            if (forwardMap.containsKey(chatInfo.getSessionId())) {//说明请求了督导，有东西
                WebSocketServer.sendMessage(SR.forward(msg.getContent(), chatInfo.getSenderName(), chatInfo.getSenderKey(), ididMap.get(chatInfo.getSessionId())), forwardMap.get(chatInfo.getSessionId()));
            }
        }else {
            //咨询师-督导聊天
            sendMessage(SR.msgSC(msg.getContent(), chatInfo.getSenderKey(), chatInfo.getSessionId()), sendTo);
            sessionIdChatSCMap.get(chatInfo.getSessionId()).getMessages().add(new Message(chatInfo.getSenderName(), chatInfo.getReceiverName(), msg.getContent(), new Date().toString()));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    private static void sendStrMessage(String message, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }

    public static void sendMessage(Object message, Session toSession) {
        String json = JSON.toJSONString(message);
        sendStrMessage(json, toSession);
    }

}
