package com.eewoo.platform.mapper;

import com.eewoo.common.pojo.Counselor;
import com.eewoo.common.pojo.Supervisor;
import com.eewoo.platform.pojo.RoughCouselor;
import com.eewoo.platform.pojo.vo.response.*;
import com.eewoo.platform.pojo.vo.response.BindCounselorResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
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
    List<BindCounselorResponse> getbindcounselors( @Param("id")Integer id );

    @Select("SELECT name FROM user_supervisor WHERE id = #{id}")
    String getName(@Param("id") Integer id);

    //遇到问题，我们并没有存放counselor和supervisor的表格，先随机返回吧。
    @Select("SELECT name,counselor.id counselor.profile FROM banned join user_supervisor join user_counselor WHERE supervisor.id = #{id}")
    List<RoughCouselor> latelyChatCounselors(Integer id);

    @Select("SELECT  id,username,banned,name,profile,consult_duration_total,consult_cnt_today, consult_duration_today, age, id_card, phone, email, work_place" +
            ",title,qualification,qualification_number " +
            " FROM user_supervisor WHERE id = #{id}")
    Supervisor getSupervisorInfo(Integer id);

    @Select("SELECT id, username, banned, name, profile, consult_duration_total,consult_cnt_total" +
            " ,consult_score_total,consult_cnt_today,consult_duration_today,age,id_card,phone,email,work_place,title" +
            " FROM user_counselor WHERE counselor_id = #{id}")
    List<Counselor> getCounselorById(@Param("id")Integer idCard);

    @Select("SELECT id, username, banned, name, profile, consult_duration_total,consult_cnt_total" +
            " ,consult_score_total,consult_cnt_today,consult_duration_today,age,id_card,phone,email,work_place,title" +
            " FROM user_counselor WHERE username = #{username}")
    List<Counselor> getConsounselorsByUsername(@Param("username")String username);

    @Select("SELECT id, username, banned, name, profile, consult_duration_total,consult_cnt_total" +
            " ,consult_score_total,consult_cnt_today,consult_duration_today,age,id_card,phone,email,work_place,title" +
            " FROM user_counselor WHERE name = #{name}")
    List<Counselor> getConsounselorsByName(@Param("name")String name);

    @Select("select day_schedule_supervisor.id ,  day_schedule_supervisor.supervisor_id , user_supervisor.username as supervisor_name , day_schedule_supervisor.day ,day_schedule_supervisor.banned from day_schedule_supervisor join user_supervisor on day_schedule_supervisor.supervisor_id =  user_supervisor.id where user_supervisor.id = #{id}")
    List<DayScheduleSupervisorResponse> getSupervisorSchedulesByDay(@Param("id") Integer id);

    @Select("SELECT schedule_supervisor.id, supervisor_id, username as supervisor_name, weekday " +
            "from schedule_supervisor join user_supervisor " +
            "on user_supervisor.id =  scheduler_supervisor.supervisor_id " +
            "where supervisor_id = #{id}")
    List<ScheduleSupervisorResponse> getSupervisorScheduleByWeek(Integer id);
}
