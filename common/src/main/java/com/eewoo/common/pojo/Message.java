package com.eewoo.common.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message {
    private String sender;
    private String receiver;
    private String message;
    private String timestamp;
}
