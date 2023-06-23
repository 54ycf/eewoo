package com.eewoo.platform.service;


import com.eewoo.platform.pojo.vo.request.*;
import com.eewoo.platform.pojo.vo.response.*;
import com.github.pagehelper.PageInfo;


import java.util.Date;
import java.util.List;

public interface AdminService {

    int disableUser(Integer id, String role);
    int enableUser(Integer id, String role);


    /**获取咨询记录**/
    PageInfo<SessionResponse> getSessions(Integer page, Integer pageSize, String name, String date);


    /**获取咨询师**/
    List<CounselorSupervisorResponse> getCounselors(Integer page,Integer pageSize);

    /**获取督导**/
    List<CounselorSupervisorResponse> getSupervisors(Integer page,Integer pageSize);

    /**删除咨询师**/
    int removeCounselor(Integer id);

    /**获取访客**/
    PageInfo<VisitorResponse> getVistors(Integer page,Integer pageSize,String name);

    /**得到咨询数量最高的咨询师**/
    List<CounselorResponse> getTopSessions();

    /**得到得分最高的咨询师**/
    List<CounselorResponse> getTopScoreCounselors();

    /**获取咨询师排班(按星期)**/
    List<ScheduleCounselorResponse> getCounselorSchedules();

    /**获取督导排班(按星期)**/
    List<ScheduleSupervisorResponse> getSupervisorSchedules();

    /**添加咨询师排班(按星期)**/
    int putCounselorSchedule(ScheduleCounselorRequest scheduleCounselorRequest);

    /**删除咨询师排班(按星期)**/
    int removeCounselorSchedule(ScheduleCounselorRequest scheduleCounselorRequest);

    /**删除督导排班(按星期)**/
    int removeSupervisorSchedule(ScheduleSupervisorRequest scheduleSupervisorRequest);

    /**更新咨询师排班(按星期)**/
    int updateCounselorSchedule(ScheduleCounselorUpdateRequest scheduleCounselorUpdateRequest);

    /**更新督导排班(按星期)**/
    int updateSupervisorSchedule(ScheduleSupervisorUpdateRequest scheduleSupervisorUpdateRequest );


    /**获取咨询师排班(按日期)**/
    List<DayScheduleCounselorResponse> getCounselorSchedulesByDay();

    /**获取督导的排班表(按日期)**/
    List<DayScheduleSupervisorResponse> getSupervisorSchedulesByDay();

    /**添加咨询师排班(按日期)**/
    int putCounselorScheduleByDay(DayScheduleCounselorRequest dayScheduleCounselorRequest);

    /**移除咨询师排班(按日期)**/
    int deleteCounselorScheduleByDay(DayScheduleCounselorRequest dayScheduleCounselorRequest);

    /**添加督导排班(按日期)**/
    int putSupervisorScheduleByDay(DayScheduleSupervisorRequest dayScheduleSupervisorRequest);

    /**移除督导排班(按日期)**/
    int deleteSupervisorScheduleByDay(DayScheduleSupervisorRequest dayScheduleSupervisorRequest);

    /**获取某个咨询师的详细信息**/
    CounselorResponse getCounselorById(Integer counselorId);

    /**获取所有咨询师，不看督导系列**/
    PageInfo<AdminCounselorResponse> getCounselorsWithoutSupervi(Integer page, Integer pageSize);

    /**根据姓名获取咨询师**/
    PageInfo<AdminCounselorResponse> getCounselorByName(String name,Integer page,Integer pageSize);

    PageInfo<AdminSupervisorResponse> getSupervisorsWithoutCounsel(Integer page,Integer pageSize);

    PageInfo<AdminSupervisorResponse> getSupervisorByName(String name,Integer page,Integer pageSize);

    /**修改咨询师和督导的绑定关系**/
    int reviseBind(BindRequest bindRequest);


    PageInfo<AdminCounselorResponse> getCounselorList(Integer page, Integer size, String name);

    PageInfo<AdminSupervisorResponse> getSupervisorList(Integer page,Integer size, String name);
}
