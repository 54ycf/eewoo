package com.eewoo.platform.pojo.vo.response;


import lombok.Data;

import java.util.List;

@Data
public class AdminCounselorResponse {
    private Integer counselorId;
    private String name;
    private String profile;
    private String title;
    private String supervisor;
    private Integer supervisorId;
    private Integer sessionCount;
    private Integer sessionTime;
    private Double sessionScore;
    private List<Integer> schedule;
    private Integer banned;

    private Integer age;
    private String idCard;
    private String phone;
    private String email;
    private String workPlace;
}
