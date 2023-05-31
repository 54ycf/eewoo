package com.eewoo.platform.pojo.vo.response;

import java.util.Date;

public class BindCounselorResponse {

    String supervisor_name; //传入参数，解析token获得,
    String name;//根据bind 表的 counselor_id 拿到 counselor name
    Date duration;//拿到session tabe的duration

    Date startTime;//startTime的全部部分

    public void setSupName(String name)
    {
        this.supervisor_name = name;
    }
}
