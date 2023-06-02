package com.eewoo.common.pojo.vo.request;


import lombok.Data;

@Data
public class VisitorCommentRequest {
    private Integer sessionId;
    private Integer visitorFeedbackScore;
    private String visitorFeedback;
}
