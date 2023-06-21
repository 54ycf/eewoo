package com.eewoo.platform.pojo.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DayScheduleCounselorResponse {
    private Integer id;
    private Integer counselorId;
    private String counselorName;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date day;
    private Integer banned;
}
