package com.eewoo.chat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatInfo {
    Boolean isValid;
    String senderKey; //role:id
    String senderName;
    String receiverKey;
    String receiverName;
    Integer sessionId;
}
