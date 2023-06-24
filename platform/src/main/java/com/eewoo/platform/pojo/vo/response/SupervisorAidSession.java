package com.eewoo.platform.pojo.vo.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SupervisorAidSession {

    Integer id;
    String supervisorName;
    Integer supervisorId;
    String counselorName;
    Integer counselorId;
    @JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss", timezone = "GMT+8")
    Date startTime;

    @JsonFormat(pattern="YYYY-MM-dd HH:mm:ss",timezone = "GMT+8")
    Date endTime;

    Integer duration;



}
