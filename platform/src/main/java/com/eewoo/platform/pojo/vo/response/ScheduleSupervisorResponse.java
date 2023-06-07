package com.eewoo.platform.pojo.vo.response;

import lombok.Data;

@Data
public class ScheduleSupervisorResponse {
    private int id;
    private int supervisorId;
    private String supervisorName;
    private int weekday;
}
