package com.eewoo.platform.pojo.vo.request;

import lombok.Data;

@Data
public class ScheduleSupervisorRequest {
    private int supervisorId;
    private int weekday;
}
