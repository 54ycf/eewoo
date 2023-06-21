package com.eewoo.platform.mapper;

import com.eewoo.common.pojo.*;
import com.eewoo.platform.pojo.vo.response.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;


@Mapper
@Repository
public interface AdminMapper {
    @Select("Select s.id as id, s.visitor_id as visitorId, uv.name as visitorName, s.counselor_id as counselorId, uc.name as counselorName, s.start_time as startTime, s.end_time as endTime, s.duration as duration, s.visitor_feedback as visitorFeedback, s.visitor_feedback_score as visitorFeedbackScore, s.counselor_feedback as counselorFeedback, s.type as type " +
            "from session as s " +
            "right join user_visitor as uv on s.visitor_id=uv.id " +
            "left join user_counselor as uc on s.counselor_id=uc.id")
    List<SessionResponse> getSessions();

    @Select("Select uc.id  as counselorId, uc.username as counselorUsername, uc.banned as counselorBanned, uc.name  as counselorName, " +
            "uc.profile  as counselorProfile, " +
            "uc.age as counselorAge, uc.id_card as counselorIdCard, uc.phone as counselorPhone, uc.email as counselorEmail," +
            " uc.work_place as counselorWorkPlace, uc.title as counselorTitle, " +
            "us.id  as supervisorId, us.username as supervisorUsername, " +
            "us.banned as supervisorBanned, us.name as supervisorName, us.profile as supervisorProfile, " +
            "us.age as supervisorAge, " +
            "us.id_card  as supervisorIdCard, us.phone as supervisorPhone, us.email as supervisorEmail," +
            " us.work_place as supervisorWorkPlace, us.title as supervisorTitle," +
            " us.qualification as supervisorQualification, us.qualification_number  as supervisorQualificationNumber from binding " +
            "right join user_counselor as uc on binding.counselor_id=uc.id " +
            "left join user_supervisor as us on binding.supervisor_id=us.id")
    List<CounselorSupervisorResponse> getCounselorsAndSupervisors();



    @Delete("Delete from user_counselor where id=#{id}")
    int deleteCounselor(Integer id);

    @Select("Select * from user_visitor")
    List<Visitor> getVisitors();

    //咨询数量排行

    @Select("Select counselor_id,count(*) as num from session group by counselor_id order by num desc limit 4")
    List<SessionResponse> getTopSessions();

    @Select("Select counselor_id,sum(visitor_feedback_score) as score from session group by counselor_id order by score desc limit 4")
    List<SessionResponse> getTopScoreCounselors();

    @Select("Select * from schedule_counselor")
    List<ScheduleCounselor> getCounselorSchedules();

    @Select("Select * from schedule_supervisor")
    List<ScheduleSupervisor> getSupervisorSchedules();

    @Select("Select * from user_supervisor where id=#{id}")
    Supervisor getSupervisor(Integer id);

    @Insert("Insert into schedule_counselor(counselor_id,weekday) values (#{counselorId},#{weekday})")
    int insertScheduleCounselor(int counselorId,int weekday);

    @Delete("delete from schedule_counselor where counselor_id=#{counselorId} and weekday=#{weekday}")
    int deleteScheduleCounselor(int counselorId,int weekday);

    @Delete("Delete from schedule_supervisor where supervisor_id=#{supervisorId} and weekday=#{weekday}")
    int deleteScheduleSupervisor(int supervisorId,int weekday);

    @Select("Select * from user_counselor where name=#{name}")
    List<Counselor> selectCounselorsByName(String name);

    @Update("update binding set supervisor_id=#{supervisorId} where counselor_id=#{counselorId}")
    int updateBind(Integer counselorId,Integer supervisorId);

    @Select("Select * from day_schedule_counselor")
    List<DayScheduleCounselorResponse> getCounselorSchedulesByDay();

    @Select("Select * from day_schedule_supervisor")
    List<DayScheduleSupervisorResponse> getSupervisorSchedulesByDay();

    @Select("Select * from schedule_counselor where counselor_id=#{counselorId} and weekday=#{weekday}")
    ScheduleCounselorResponse getScheduleCounselor(int counselorId,int weekday);

    @Select("Select * from schedule_supervisor where supervisor_id=#{supervisorId} and weekday=#{weekday}")
    ScheduleSupervisorResponse getScheduleSupervisor(int supervisorId,int weekday);

    @Select("Select * from day_schedule_counselor where counselor_id=#{counselorId} and day=#{day}")
    DayScheduleCounselorResponse getScheduleCounselorByDay(int counselorId, Date day);

    @Update("update day_schedule_counselor set banned=0 where counselor_id=#{counselorId} and day=#{day}")
    int updateScheduleCounselorByDay(int counselorId,Date day);

    @Update("update day_schedule_counselor set banned=1 where counselor_id=#{counselorId} and day=#{day}")
    int deleteScheduleCounselorByDay(int counselorId,Date day);

    @Insert("Insert into day_schedule_counselor (counselor_id,day,banned) values (#{counselorId},#{day},0)")
    int insertScheduleCounselorByDay(int counselorId,Date day);

    @Select("Select * from day_schedule_counselor where counselor_id=#{counselorId} and day=#{day}")
    DayScheduleCounselorResponse selectDayScheduleCounselor(int counselorId,Date day);

    @Select("Select * from day_schedule_supervisor where supervisor_id=#{supervisorId} and day=#{day}")
    DayScheduleSupervisorResponse selectDayScheduleSupervisor(int supervisorId,Date day);

    @Insert("Insert into day_schedule_supervisor (supervisor_id,day,banned) values (#{supervisorId},#{day},0)")
    int insertScheduleSupervisorByDay(int supervisorId,Date day);

    @Update("update day_schedule_supervisor set banned=0 where supervisor_id=#{supervisorId} and day=#{day}")
    int updateScheduleSupervisorByDay(int supervisorId,Date day);

    @Update("update day_schedule_supervisor set banned=1 where supervisor_id=#{supervisorId} and day=#{day}")
    int deleteScheduleSupervisorByDay(int supervisorId,Date day);

    /**根据bind获取督导的id**/
    @Select("Select supervisor_id from binding where counselor_id=#{counselorId}")
    Integer selectSuperviIdByBind(Integer counselorId);

    /**根据id获取咨询师**/
    @Select("Select name from user_supervisor where id=#{supervisor_id}")
    String selectSuperviNameById(Integer supervisorId);

    /**获取某个咨询师的咨询师总数**/
    @Select("Select count(*) from session where counselor_id=#{counselorId}")
    Integer selectSessionCountById(Integer counselorId);

    /**获取咨询师咨询的总时长**/
    @Select("Select count(duration) from session where counselor_id=#{counselorId}")
    Integer selectSessionTimeById(Integer counselorId);

    /**获取某个咨询师的平均得分**/
    @Select("Select avg(visitor_feedback_score) from session where counselor_id=#{counselorId}")
    Double countAvgSessionScoreById(Integer counselorId);

    /**获取某个咨询师的值班安排**/
    @Select("Select weekday from schedule_counselor where counselor_id=#{counselorId}")
    List<Integer> getCounselorScheduleById(Integer counselorId);

    /**获取所有督导**/
    @Select("Select * from user_supervisor")
    List<Supervisor> getSupervisors();

    /**根据counselorId获取督导id**/
    @Select("Select counselor_id from binding where supervisor_id=#{supervisorId}")
    List<Integer> selectCounselorIdByBind(Integer supervisorId);

    /**根据id获取counselor**/
    @Select("Select * from user_counselor where id=#{counselorId}")
    Counselor selectCounselorById(int counselorId);

    /**根据id获取督导的排班**/
    @Select("Select weekday from schedule_supervisor where supervisor_id=#{supervisorId}")
    List<Integer> getSupervisorScheduleById(Integer supervisorId);

    /**根据姓名获取督导**/

    @Select("Select * from user_supervisor where name=#{name}")
    List<Supervisor> selectSupervisorsByName(String name);


    List<AdminCounselorResponse> getCounselorList(String name);
}
