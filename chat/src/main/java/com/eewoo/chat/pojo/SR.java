package com.eewoo.chat.pojo;

import com.eewoo.common.pojo.Message;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SocketResponse websocket的返回体
 */
@Data
public class SR {
    String type; //message notify chatToken err info
    Object data;

    public static SR msg(String content, String from){
        SR response = new SR();
        response.setType("message");
        Map<String, String> map = new HashMap<>();
        map.put("content", content);
        map.put("from", from);
        response.setData(map);
        return response;
    }

    public static SR msgInit(List<Message> messageList, Integer sessionId){
        SR response = new SR();
        response.setType("messageSCInit");
        Map<String, Object> map = new HashMap<>();
        map.put("messageList", messageList);
        map.put("sessionId", sessionId);
        response.setData(map);
        return response;
    }

    public static SR msgSC(String content, String from, Integer sessionId){
        SR response = new SR();
        response.setType("messageSC");
        Map<String, String> map = new HashMap<>();
        map.put("content", content);
        map.put("from", from);
        map.put("sessionId", String.valueOf(sessionId));
        response.setData(map);
        return response;
    }

    public static SR forward(String content, String from, String fromId, Integer sessionId) {
        SR response = new SR();
        response.setType("forward");
        Map<String, Object> map = new HashMap<>();
        map.put("content", content);
        map.put("from", from);
        map.put("fromId", fromId);
        map.put("sessionId", String.valueOf(sessionId));
        response.setData(map);
        return response;
    }

    /**
     * 提醒做评价
     * @param chatToken
     * @return
     */
    public static SR commentNotify(String chatToken){
        SR response = new SR();
        response.setType("commentNotify");
        response.setData(chatToken);
        return response;
    }

    public static SR endSCNotify(Integer sessionId){
        SR response = new SR();
        response.setType("endSCNotify");
        response.setData(sessionId);
        return response;
    }

    /**
     * 提醒10min未回话了，快点说话，否则五分钟后会自动退出
     * @param chatToken
     * @return
     */
    public static SR boringNotify(String chatToken){
        SR response = new SR();
        response.setType("boringNotify");
        response.setData(chatToken);
        return response;
    }

    public static SR chatToken(String chatToken, String toUserKey/*v:1*/, String toUsername, Integer sessionId){
        SR response = new SR();
        response.setType("chatToken");
        Map<String, String> map = new HashMap<>();
        map.put("chatToken", chatToken);
        map.put("toUserKey", toUserKey);
        map.put("toUserName", toUsername);
        map.put("sessionId", String.valueOf(sessionId));
        response.setData(map);
        return response;
    }

    public static SR chatTokenSC(String chatToken, String toUserKey/*v:1*/, String toUsername, Integer baseSessionId, Integer sessionId){
        SR response = new SR();
        response.setType("chatTokenSC");
        Map<String, String> map = new HashMap<>();
        map.put("chatToken", chatToken);
        map.put("toUserKey", toUserKey);
        map.put("toUserName", toUsername);
        map.put("baseSessionId", String.valueOf(baseSessionId));
        map.put("sessionId", String.valueOf(sessionId));
        response.setData(map);
        return response;
    }

    public static SR err(String info){
        SR response = new SR();
        response.setType("err");
        response.setData(info);
        return response;
    }

    public static SR info(String info){
        SR response = new SR();
        response.setType("info");
        response.setData(info);
        return response;
    }


}
