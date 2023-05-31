package com.eewoo.platform.pojo.vo.response;

import com.alibaba.druid.support.monitor.annotation.MTable;


public class Consult {


    String visitorName;
    String startTime;
    String endTime;
    /**
     * 在session表中只有visitorID，需要另一张表得到visitor的name
     * 更具体的信息，确定点入某资讯列表后由chat提供记录；
     */
    String duration;
}
