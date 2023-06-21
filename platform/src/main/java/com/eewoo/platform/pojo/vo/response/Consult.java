package com.eewoo.platform.pojo.vo.response;

import com.alibaba.druid.support.monitor.annotation.MTable;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;


public class Consult {

    Integer visitorId;
    String visitorName;
    /**
     * 在session表中只有visitorID，需要另一张表得到visitor的name
     * 更具体的信息，确定点入某咨询列表后由chat提供记录；
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endTime;
    Integer duration;
}
