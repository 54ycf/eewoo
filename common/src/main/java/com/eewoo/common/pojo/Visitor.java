package com.eewoo.common.pojo;

import lombok.Data;

@Data
public class Visitor extends User{
    String phone;
    String emergencyPerson;
    String emergencyContact;
}
