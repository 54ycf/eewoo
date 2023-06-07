package com.eewoo.platform.pojo.vo.response;

import lombok.Data;

@Data
public class ScheduleCounselorResponse {
    private int id;
    private int counselorId;
    private String counselorName;
    private int weekday;

}
