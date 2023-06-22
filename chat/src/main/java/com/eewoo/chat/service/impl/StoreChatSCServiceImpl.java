package com.eewoo.chat.service.impl;



import com.eewoo.chat.pojo.ChatSC;
import com.eewoo.chat.service.StoreChatSCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreChatSCServiceImpl implements StoreChatSCService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(ChatSC chat) {
        mongoTemplate.save(chat);
        System.out.println("finished!");
    }

    @Override
    public ChatSC findById(Integer id) {
        Query query=new Query();
        Criteria criteria=Criteria.where("id").is(id);
        query.addCriteria(criteria);
        return mongoTemplate.findOne(query,ChatSC.class);
    }

    @Override
    public List<ChatSC> findByParticipants(List<String> participants) {
        Query query=new Query();
        Criteria criteria=Criteria.where("participants").is(participants);
        query.addCriteria(criteria);
        return mongoTemplate.find(query,ChatSC.class);
    }
}
