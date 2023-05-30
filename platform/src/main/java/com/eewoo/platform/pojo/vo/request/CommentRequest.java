package com.eewoo.platform.pojo.vo.request;


import lombok.Data;

@Data
public class CommentRequest {
    private Integer sessionId;
    private Integer visitorFeedbackScore;
    private String visitorFeedback;
}
