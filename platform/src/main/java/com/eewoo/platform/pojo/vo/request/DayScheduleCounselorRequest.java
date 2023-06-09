package com.eewoo.platform.pojo.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class DayScheduleCounselorRequest {
    private int counselorId;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date date;
}
