package com.eewoo.chat.service;

import com.eewoo.common.pojo.Chat;

import java.util.List;

public interface StoreChatService {

    void save(Chat chat);

    Chat findById(Integer id);

    List<Chat> findByParticipants(List<String> participants);

}
