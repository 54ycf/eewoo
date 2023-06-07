package com.eewoo.common.pojo;

import lombok.Data;

@Data
public class ScheduleSupervisor {
    private int id;
    private int supervisorId;
    private int weekday;
}
