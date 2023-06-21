package com.eewoo.platform.pojo.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DayScheduleSupervisorResponse {
    private Integer id;
    private Integer supervisorId;
    private String supervisorName;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date day;
    private Integer banned;
}
