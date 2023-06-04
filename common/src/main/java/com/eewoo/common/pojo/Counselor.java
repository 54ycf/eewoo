package com.eewoo.common.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Counselor extends User{
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
