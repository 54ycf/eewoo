package com.eewoo.chat.service.impl;

import com.eewoo.chat.service.StoreChatService;
import com.eewoo.common.pojo.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StoreChatServiceImpl implements StoreChatService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Chat chat) {
        mongoTemplate.save(chat);
        System.out.println("finished!");
    }

    @Override
    public Chat findById(Integer id) {
        Query query=new Query();
        Criteria criteria=Criteria.where("id").is(id);
        query.addCriteria(criteria);
        return mongoTemplate.findOne(query,Chat.class);
    }

    @Override
    public List<Chat> findByParticipants(List<String> participants) {
        Query query=new Query();
        Criteria criteria=Criteria.where("participants").is(participants);
        query.addCriteria(criteria);
        return mongoTemplate.find(query,Chat.class);
    }
}
