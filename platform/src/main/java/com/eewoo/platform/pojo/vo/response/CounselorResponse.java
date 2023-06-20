package com.eewoo.platform.pojo.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CounselorResponse {
    Integer id;
    String username;
    Integer banned;
    String name;
    String profile;
    Integer age;
    String idCard;
    String phone;
    String email;
    String workPlace;
    String title;
    Integer sessionCount;
    Double sessionScore;

}
