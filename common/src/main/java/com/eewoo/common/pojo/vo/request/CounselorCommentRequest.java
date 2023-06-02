package com.eewoo.common.pojo.vo.request;

import lombok.Data;

@Data
public class CounselorCommentRequest {
    Integer sessionId;
    String feedback;
    String type;

}
