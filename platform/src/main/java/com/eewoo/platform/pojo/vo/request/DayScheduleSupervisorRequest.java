package com.eewoo.platform.pojo.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DayScheduleSupervisorRequest {
    private int supervisorId;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date day;
}
