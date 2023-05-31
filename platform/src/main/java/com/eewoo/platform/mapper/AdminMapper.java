package com.eewoo.platform.mapper;

import com.eewoo.common.pojo.Session;
import com.eewoo.platform.pojo.vo.response.CounselorSupervisorResponse;
import com.eewoo.platform.pojo.vo.response.SessionResponse;
import org.apache.ibatis.annotations.Delete;
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
            "uc.profile  as counselorProfile, uc.consult_duration_total  as counselorConsultDurationTotal," +
            " uc.consult_cnt_total  as counselorConsultCntTotal, uc.consult_score_total  as counselorConsultScoreTotal," +
            " uc.consult_cnt_today  as counselorConsultCntToday, uc.consult_duration_today   as counselorConsultDurationToday, " +
            "uc.age as counselorAge, uc.id_card as counselorIdCard, uc.phone as counselorPhone, uc.email as counselorEmail," +
            " uc.work_place as counselorWorkPlace, uc.title as counselorTitle, " +
            "us.id  as supervisorId, us.username as supervisorUsername, us.password as supervisorPassword, " +
            "us.banned as supervisorBanned, us.name as supervisorName, us.profile as supervisorProfile, " +
            "us.consult_duration_total as supervisorConsultDurationTotal, us.consult_cnt_today  as supervisorConsultCntToday," +
            " us.consult_duration_today as supervisorConsultDurationToday, us.age as supervisorAge, " +
            "us.id_card  as supervisorIdCard, us.phone as supervisorPhone, us.email as supervisorEmail," +
            " us.work_place as supervisorWorkPlace, us.title as supervisorTitle," +
            " us.qualification as supervisorQualification, us.qualification_number  as supervisorQualificationNumber from binding " +
            "right join user_counselor as uc on binding.counselor_id=uc.id " +
            "left join user_supervisor as us on binding.supervisor_id=us.id")
    List<CounselorSupervisorResponse> getCounselorsAndSupervisors();



    @Delete("Delete from user_counselor where id=#{id}")
    int deleteCounselor(Integer id);

}
