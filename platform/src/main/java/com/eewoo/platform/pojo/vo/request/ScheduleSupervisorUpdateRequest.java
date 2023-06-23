package com.eewoo.platform.pojo.vo.request;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleSupervisorUpdateRequest {
    private int supervisorId;
    private List<Integer> weekdays;
}
