package com.eewoo.platform.pojo.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class SessionResponse {
    private int id;
    private String visitorId;
    private int counselorId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date duration;
    private String visitorFeedback;
    private int visitorFeedbackScore;
    private String counselorFeedback;
    private String type;
}
