package com.eewoo.platform.pojo.vo.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


@Data
public class DayScheduleCounselorRequest {
    private int counselorId;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date day;
}
