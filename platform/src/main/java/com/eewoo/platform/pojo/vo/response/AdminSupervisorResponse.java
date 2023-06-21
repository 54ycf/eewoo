package com.eewoo.platform.pojo.vo.response;


import com.eewoo.common.pojo.Counselor;
import lombok.Data;

import java.util.List;

@Data
public class AdminSupervisorResponse {
    private  Integer supervisorId;
    private String name;
    private String profile;
    private String identity="督导";
    private List<Counselor> counselors;
    private List<Integer> schedule;
    private Integer banned;
    private Integer age;
    private String idCard;
    private String phone;
    private String email;
    private String workPlace;
    private String qualification;
    private String qualificationNumber;
    private String title;
}
