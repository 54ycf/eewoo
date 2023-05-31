package com.eewoo.chat.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class MessageSend {
    String content;
    String chatToken;
}
