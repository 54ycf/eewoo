package com.eewoo.platform.mapper;

import com.eewoo.common.pojo.Counselor;
import com.eewoo.common.pojo.Supervisor;
import com.eewoo.platform.pojo.vo.response.Consult;
import com.eewoo.platform.pojo.vo.response.DayScheduleCounselorResponse;
import com.eewoo.platform.pojo.vo.response.ScheduleCounselorResponse;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:"
 */
@Mapper
@Repository
public interface CounselorMapper {

    @Update("UPDATE session SET counselor_feedback=#{feedback}, type = #{type} WHERE id = #{sessionID}")
    int createEvaluation(@Param("sessionID")Integer sessionID, @Param("feedback") String feedback, @Param("type")String type);

    @Select("SELECT * FROM binding WHERE counselor_id = #{counselorId}")
    Supervisor findSupervisor(@Param("counselorId") Integer counselorId);


    /**
     * 获取在session表的，并且还是与本咨询师交流的用户
     * @param counselorId
     * @return
     */
    @Select("SELECT visitor_id, user_visitor.username as visitor_name, start_time, end_time, duration " +
            "FROM session join user_visitor on session.visitor_id = user_visitor.id WHERE counselor_id = #{id}")
    List<Consult> getAllConsults(@Param("id") Integer counselorId);

    @Update("UPDATE session SET end_time = #{endTime}, duration = (UNIX_TIMESTAMP(end_time) - UNIX_TIMESTAMP(start_time))/60 WHERE id = #{sessionId}")
    int endSession(Integer sessionId, Date endTime);

    @Select("SELECT SUM( duration ) FROM session WHERE counselor_id = #{id}")
    Integer getTotalSessionTime(@Param("id") Integer id);


    @Select("SELECT COUNT(*) FROM session WHERE counselor_id = #{id} and TO_DAYS(start_time) = TO_DAYS(NOW())")
    Integer getTodaySessionNum(@Param("id") Integer id, @Param("start_time") Date today);//今日的咨询师，参数是id和今天的日期

    @Select("SELECT SUM(duration) FROM session WHERE counselor_id = #{id} and TO_DAYS(start_time) = TO_DAYS(NOW())")
    Integer getTodaySessionTime(@Param("id") Integer cId);

    @Select("SELECT id, username, banned, name, profile, " +
            "age,id_card,phone,email,work_place,title" +
            " FROM user_counselor WHERE id = #{id}")
    Counselor getmassiveInfo(@Param("id")Integer id);


    /**
     * 要获取咨询师的排班信息，还要去另一个user_counselor获取他的名字、urusai
     * @param id
     * @return
     */
    @Select("select schedule_counselor.id,schedule_counselor.counselor_id,user_counselor.username as counselor_name, schedule_counselor.weekday " +
            "from schedule_counselor join user_counselor on schedule_counselor.counselor_id = user_counselor.id where schedule_counselor.counselor_id = #{id}")
    List<ScheduleCounselorResponse> getPersonalScheduleByWeek(Integer id);

    @Select("select day_schedule_counselor.id ,  day_schedule_counselor.counselor_id , user_counselor.username as counselor_name , day_schedule_counselor.day ,day_schedule_counselor.banned from day_schedule_counselor " +
            "join user_counselor on day_schedule_counselor.counselor_id =  user_counselor.id " +
            "where user_counselor.id = #{id}")
    List<DayScheduleCounselorResponse> getPersonalScheduleByDay(Integer id);
}
