package com.eewoo.platform.pojo.vo.response;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class SupervisorAidSession {
    String supervisorName;
    Integer supervisorId;
    String counselorName;
    Integer counselorId;
    @JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss", timezone = "GMT+8")
    Date start_time;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    Date end_time;

    Integer duration;



}
