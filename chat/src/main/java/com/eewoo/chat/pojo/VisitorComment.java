package com.eewoo.chat.pojo;

import lombok.Data;

@Data
public class VisitorComment {
    String chatToken;
    String visitorFeedback;
    Integer visitorFeedbackScore;
}
