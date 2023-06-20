package com.eewoo.platform.pojo.vo.response;

import lombok.Data;

@Data
public class VisitorResponse {
    Integer id;
    Integer banned;
    String username;
    String name;
    String profile;
    String phone;
    String emergencyPerson;
    String emergencyContact;
}
