package com.eewoo.common.pojo;

import lombok.Data;

@Data
public class Supervisor extends User{
    Integer consultDurationTotal;
    Integer consultCntToday;
    Integer consultDurationToday;
    Integer age;
    String idCard;
    String phone;
    String email;
    String workPlace;
    String title;
    String qualification;
    String qualificationNumber;
}
