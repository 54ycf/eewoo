package com.eewoo.platform.service;

import com.eewoo.platform.pojo.vo.response.Consult;
import com.eewoo.platform.pojo.vo.response.SupervisorResponse;

import java.util.List;

public interface CounselorService {
    public  List<Consult> getConsult(); ;

    int createEvaluation(Integer sessionID, String feedback, String type);

    SupervisorResponse findSupervisor(Integer counselorId);


}
