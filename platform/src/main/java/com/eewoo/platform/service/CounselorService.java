package com.eewoo.platform.service;

import com.eewoo.common.pojo.Counselor;
//import com.eewoo.common.pojo.Session;
import com.eewoo.platform.pojo.vo.response.Consult;
import com.eewoo.platform.pojo.vo.response.SupervisorResponse;

import java.util.List;

public interface CounselorService {
    public  List<Consult> getConsult(Integer page, Integer size);

    int createEvaluation(Integer sessionID, String feedback, String type);

    SupervisorResponse findSupervisor(Integer counselorId);


    Integer getTotalSessionTime( );

    Integer getTodaySessionNum();

    Integer getTodaySessionTime();//获取今天总的咨询时长

    Counselor getAllInfoExceptPassword();

//    Session fetchSessionIfAuthenticated(Integer item);
}
