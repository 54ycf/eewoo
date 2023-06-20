package com.eewoo.platform.pojo.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class BindCounselorResponse {
    //    Date duration;//拿到session tabe的duration
//    Date startTime;//startTime的全部部分
//    String supervisor_name; //传入参数，解析token获得,


    public String username;//根据bind 表的 counselor_id 拿到 counselor name
    Integer counselorId;
    public  String status;

    BindCounselorResponse()
    {
        status = "offline";
    }

}
