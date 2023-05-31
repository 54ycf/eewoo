package com.eewoo.chat.service.impl;

import com.eewoo.chat.service.ChatService;
import com.eewoo.common.pojo.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


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
}
