package com.eewoo.chat.service;

import com.eewoo.chat.pojo.ChatInfo;
import com.eewoo.chat.pojo.SR;
import com.eewoo.chat.socket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 定时器，监测聊天是否超时
 */
@Component
public class ChatScheduler {
    @Autowired
    ChatService chatService;

    private static final Map<String, LocalDateTime> lastUpdateTimeMap =  new ConcurrentHashMap<>();

    @Scheduled(fixedDelay = 60000) //每隔一分钟检查一下是否有chatToken超时
    public void checkChatTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        lastUpdateTimeMap.forEach((chatToken, lastUpdateTime) -> {
            Duration durationSinceLastUpdate = Duration.between(lastUpdateTime, currentTime);
            if (durationSinceLastUpdate.toMinutes() >= 15){ //超过十五分钟，直接结束会话
                chatService.endVCSession(chatToken);
            }else if (durationSinceLastUpdate.toMinutes() >= 10) { //超过十分钟未聊天 通知用户是否结束
                sendNotification(chatToken);
            }
        });
    }

    public static void updateTime(String chatToken) {
        // 更新数据时更新最后更新时间
        lastUpdateTimeMap.put(chatToken, LocalDateTime.now());
    }

    public static void removeChatToken(String chatToken){
        lastUpdateTimeMap.remove(chatToken);
    }

    private void sendNotification(String chatToken) {
        ChatInfo chatInfo = WebSocketServer.tokenInfoMap.get(chatToken);
        Session sessionTo = WebSocketServer.sessionMap.get(chatInfo.getSenderKey());
        WebSocketServer.sendMessage(SR.boringNotify(chatToken), sessionTo);
    }

}


