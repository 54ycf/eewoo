package com.eewoo.platform.service;


import com.eewoo.common.pojo.ScheduleCounselor;
import com.eewoo.platform.pojo.vo.request.ScheduleCounselorRequest;
import com.eewoo.platform.pojo.vo.request.ScheduleSupervisorRequest;
import com.eewoo.platform.pojo.vo.response.*;


import java.util.List;

public interface AdminService {

    int disableUser(Integer id, String role);

    int enableUser(Integer id, String role);

    List<SessionResponse> getSessions();

    List<CounselorSupervisorResponse> getCounselors();

    List<CounselorSupervisorResponse> getSupervisors();

    int removeCounselor(Integer id);

    List<VisitorResponse> getVistors();

    List<CounselorResponse> getTopSessions();

    List<CounselorResponse> getTopScoreCounselors();

    List<ScheduleCounselorResponse> getCounselorSchedules();

    List<ScheduleSupervisorResponse> getSupervisorSchedules();

    int putCounselorSchedule(ScheduleCounselorRequest scheduleCounselorRequest);

    int removeCounselorSchedule(ScheduleCounselorRequest scheduleCounselorRequest);

    int removeSupervisorSchedule(ScheduleSupervisorRequest scheduleSupervisorRequest);


}
