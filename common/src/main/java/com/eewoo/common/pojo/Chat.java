package com.eewoo.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("chat")
public class Chat {
    @Id
    private Long id;
    private List<String> participants;
    private List<Message> messages;
}
