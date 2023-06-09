package com.eewoo.platform.service;

import com.eewoo.common.pojo.Counselor;
import com.eewoo.common.pojo.vo.request.SessionRequest;
import com.eewoo.platform.pojo.vo.response.CounselorResponse;
import com.eewoo.platform.pojo.vo.response.SessionResponse;
import com.eewoo.platform.pojo.vo.response.VisitorResponse;

import java.util.Date;
import java.util.List;

public interface VisitorService {

    /**获取访客信息**/
    VisitorResponse getInfo();

    /**获取所有咨询师**/
    List<CounselorResponse> getCounselors();

    /**获取咨询过的历史咨询师**/
    List<CounselorResponse> getHistoryCounselors();

    /**给咨询师评价**/
    void giveCounselorComment(Integer sessionId,String feedback,Integer score);

    /**创建会话**/
    int createSession(SessionRequest session);

    /**获取历史会话记录**/
    List<SessionResponse> getHistorySessions();

}
