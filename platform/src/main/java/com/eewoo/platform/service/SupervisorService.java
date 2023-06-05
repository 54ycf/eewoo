package com.eewoo.platform.service;

import com.eewoo.common.pojo.Counselor;
import com.eewoo.common.pojo.Supervisor;
import com.eewoo.platform.pojo.RoughCouselor;
import com.eewoo.platform.pojo.vo.request.FindCounselorMsg;
import com.eewoo.platform.pojo.vo.response.BindCounselorResponse;
import com.eewoo.platform.pojo.vo.response.CounselorResponse;
import com.eewoo.platform.pojo.vo.response.VisitorResponse;


import java.util.List;

public interface SupervisorService {
    List<VisitorResponse> getVisitors();
    List<CounselorResponse> getCounselors();

    List<VisitorResponse> getVisitorList();

    List<BindCounselorResponse> bindCounselorsList(Integer page, Integer size);

    List<RoughCouselor> getLatelyChatCounselors();

    Supervisor getSupervisorInfo();

    List<Counselor> findCounselors(FindCounselorMsg fcm);
}
