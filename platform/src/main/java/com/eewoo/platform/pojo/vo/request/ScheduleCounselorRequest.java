package com.eewoo.platform.pojo.vo.request;


import lombok.Data;

import java.util.List;

@Data
public class ScheduleCounselorRequest {
    private int counselorId;
    private int weekday;
}
