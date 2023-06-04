package com.eewoo.platform.pojo.vo.response;


import lombok.Data;

import java.util.Date;

@Data
public class CounselorResponse {
    Integer id;
    String username;
    Integer banned;
    String name;
    String profile;
    Date consultDurationTotal;
    Integer consultCntTotal;
    Integer consultScoreTotal;
    Integer consultCntToday;
    Integer consultDurationToday;
    Integer age;
    String idCard;
    String phone;
    String email;
    String workPlace;
    String title;
}

