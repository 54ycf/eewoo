package com.eewoo.chat.service;

import com.eewoo.chat.pojo.ChatSC;

import java.util.List;

public interface StoreChatSCService {
    void save(ChatSC chat);

    ChatSC findById(Integer id);

    List<ChatSC> findByParticipants(List<String> participants);
}
