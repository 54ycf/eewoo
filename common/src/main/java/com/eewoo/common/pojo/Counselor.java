package com.eewoo.common.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Counselor extends User{

    Integer age;
    String idCard;
    String phone;
    String email;
    String workPlace;
    String title;

}
