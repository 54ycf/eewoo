package com.eewoo.platform.pojo.vo.response;

import lombok.Data;

@Data
public class ScheduleCounselorResponse {
    private int id; //schedule_counselor.id
    private int counselorId;//schedule_counselor.counselor_id
    private String counselorName;//user_counselor.username
    private int weekday;//schedule_counselor.weekday

}
