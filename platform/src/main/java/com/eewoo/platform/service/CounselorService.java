package com.eewoo.platform.service;
import com.eewoo.common.pojo.Counselor;
import com.eewoo.common.pojo.Supervisor;
import com.eewoo.common.pojo.vo.request.SessionSCRequest;
import com.eewoo.platform.pojo.vo.response.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CounselorService {
    public PageInfo<Consult> getConsult(Integer page, Integer size);

    /**
     * 咨询师创建会话评价内容，填入此次会话的类型type
     * @param sessionID
     * @param feedback
     * @param type
     * @return
     */
    int createEvaluation(Integer sessionID, String feedback, String type);

    SupervisorResponse findSupervisor(Integer counselorId);

    Integer getTotalSessionTime( );

    Integer getTodaySessionNum();

    Integer getTodaySessionTime();//获取今天总的咨询时长

    Counselor getAllInfoExceptPassword();

    List<ScheduleCounselorResponse> getCounselorSchedules();

    List<DayScheduleCounselorResponse> getSuperVisorSchedulesByDay();

    Supervisor getBindSupervisor();


    Integer createSCSessionAndFetchID(SessionSCRequest sessionSCRequest);

    PageInfo<SessionResponse> getCounselorRelatedSession(Integer page, Integer size, String name, String date);
}
