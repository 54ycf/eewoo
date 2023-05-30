package com.eewoo.platform.service;

import com.eewoo.common.pojo.Session;
import com.eewoo.platform.pojo.vo.request.SessionRequest;
import com.eewoo.platform.pojo.vo.response.CounselorResponse;
import com.eewoo.platform.pojo.vo.response.SessionResponse;
import com.eewoo.platform.pojo.vo.response.VisitorResponse;

import java.util.Date;
import java.util.List;

public interface VisitorService {

    VisitorResponse getInfo();

    List<CounselorResponse> getCounselors();

    List<CounselorResponse> getHistoryCounselors();

    void giveCounselorComment(Integer sessionId,String feedback,Integer score);

    int createSession(Session session);

    List<SessionResponse> getHistorySessions();

}
