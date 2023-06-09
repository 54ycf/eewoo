package com.eewoo.platform.mapper;

import com.eewoo.common.pojo.ScheduleCounselor;
import com.eewoo.common.pojo.ScheduleSupervisor;
import com.eewoo.common.pojo.Supervisor;
import com.eewoo.common.pojo.Visitor;
import com.eewoo.platform.pojo.vo.response.CounselorSupervisorResponse;
import com.eewoo.platform.pojo.vo.response.SessionResponse;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


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
}
