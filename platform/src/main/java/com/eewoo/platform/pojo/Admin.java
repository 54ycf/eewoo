package com.eewoo.platform.pojo;

import com.eewoo.common.pojo.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class Admin extends User implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;

    String profile;
}
