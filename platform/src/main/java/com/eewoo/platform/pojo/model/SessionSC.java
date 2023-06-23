package com.eewoo.platform.pojo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SessionSC {

    int id;
    int counselorId;
    int supervisorId;

    @JsonFormat(pattern = "YYYY-mm-dd HH:mm:ss", timezone = "GMT+8")
    Date startTime;

    @JsonFormat(pattern = "YYYY-mm-dd HH:mm:ss", timezone = "GMT+8")
    Date endTime;

    int duration;
}
