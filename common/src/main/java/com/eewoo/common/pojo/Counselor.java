package com.eewoo.common.pojo;

import lombok.Data;

@Data
public class Counselor extends User{
    Integer consultDurationTotal;
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
