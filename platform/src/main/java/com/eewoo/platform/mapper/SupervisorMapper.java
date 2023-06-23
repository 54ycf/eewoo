package com.eewoo.platform.mapper;

import com.eewoo.common.pojo.Counselor;
import com.eewoo.common.pojo.Supervisor;
import com.eewoo.platform.pojo.RoughCouselor;
import com.eewoo.platform.pojo.vo.response.*;
import com.eewoo.platform.pojo.vo.response.BindCounselorResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface SupervisorMapper {
    @Select("SELECT id , username, name, profile, phone,  emergency_person,emergency_contact FROM user_visitor ")
    List<VisitorResponse> getVisitors();

    @Select("SELECT id , username, name, banned, profile, phone, email FROM user_counselor ")
    List<CounselorResponse> getCounselors();

    //bind counselor 's status name ,id but also need session figures
    @Select("SELECT counselor_id ,user_counselor.username FROM binding join user_counselor on binding.counselor_id = user_counselor.id where supervisor_id = #{id} ")
    List<BindCounselorResponse> getBindcounselors(@Param("id")Integer id );

    @Select("SELECT name FROM user_supervisor WHERE id = #{id}")
    String getName(@Param("id") Integer id);

    //遇到问题，我们并没有存放counselor和supervisor的表格，先随机返回吧。
    @Select("SELECT name,counselor.id, counselor.profile FROM banned join user_supervisor join user_counselor WHERE supervisor.id = #{id}")
    List<RoughCouselor> latelyChatCounselors(Integer id);

    @Select("SELECT  id,username,banned,name,profile,consult_duration_total,consult_cnt_today, consult_duration_today, age, id_card, phone, email, work_place" +
            ",title,qualification,qualification_number " +
            " FROM user_supervisor WHERE id = #{id}")
    Supervisor getSupervisorInfo(Integer id);

    @Select("SELECT id, username, banned, name, profile, age,id_card,phone,email,work_place,title FROM user_counselor WHERE id_card = #{id}")
    List<Counselor> getCounselorById(@Param("id")String idCard);

    @Select("SELECT id, username, banned, name, profile, age,id_card,phone,email,work_place,title FROM user_counselor WHERE username = #{username} ")
    List<Counselor> getConsounselorsByUsername(@Param("username")String username);

    /**
     * 名字支持模糊查询，username和id是不支持的，这里返回的是列表，ok的
     * @param name
     * @return
     */
    @Select("SELECT id, username, banned, name, profile, age,id_card,phone,email,work_place,title FROM user_counselor WHERE name like '%#{name}%'")
    List<Counselor> getConsounselorsByName(@Param("name")String name);

    @Select("select day_schedule_supervisor.id ,  day_schedule_supervisor.supervisor_id , user_supervisor.username as supervisor_name , day_schedule_supervisor.day ,day_schedule_supervisor.banned from day_schedule_supervisor join user_supervisor on day_schedule_supervisor.supervisor_id =  user_supervisor.id where user_supervisor.id = #{id}")
    List<DayScheduleSupervisorResponse> getSupervisorSchedulesByDay(@Param("id") Integer id);

    @Select("SELECT schedule_supervisor.id, supervisor_id, username as supervisor_name, weekday " +
            "from schedule_supervisor join user_supervisor " +
            "on user_supervisor.id =  schedule_supervisor.supervisor_id " +
            "where supervisor_id = #{id}")
    List<ScheduleSupervisorResponse> getSupervisorScheduleByWeek(Integer id);

    @Select("select #{username} as supervisor_name , supervisor_id, user_counselor.username as counselor_name, counselor_id, start_time, end_time, duration from session_sc join user_counselor on session_sc.counselor_id = user_counselor.id where supervisor_id = #{id} ")
    List<SupervisorAidSession> fetchSupervisorAidSession(Integer id, String username);
    //传入督导的名字，减少join一张不必要的督导账户表格了

    @Update("update session_sc set end_time = #{date}, duration = #{duration} where id = #{sessionId} ")
    void endSCSessionInMapper(Integer sessionId, Date date, int duration);


    @Select("select start_time from session_sc where id = #{sessionId}")
    Date getStartTimeById(Integer sessionId);
}
