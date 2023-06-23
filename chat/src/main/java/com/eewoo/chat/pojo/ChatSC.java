package com.eewoo.chat.pojo;

import com.eewoo.common.pojo.Message;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("chat_sc")
public class ChatSC {
    @Id
    private Long id;
    private List<String> participants = Lists.newArrayList();
    private List<Message> messages = Lists.newArrayList();
}
