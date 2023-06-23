package com.eewoo.platform.pojo.vo.request;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleCounselorUpdateRequest {
    private int counselorId;
    private List<Integer> weekdays;
}
