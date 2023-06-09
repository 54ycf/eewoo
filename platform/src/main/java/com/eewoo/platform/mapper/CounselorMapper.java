package com.eewoo.platform.mapper;

import com.eewoo.common.pojo.Counselor;
//import com.eewoo.common.pojo.Session;
import com.eewoo.common.pojo.Supervisor;
import com.eewoo.platform.pojo.vo.response.Consult;
import org.apache.ibatis.annotations.*;
import org.springframework.context.annotation.Bean;
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
    @Select("SELECT name as visitorName, start_time, end_time, duration " +
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

    @Select("SELECT id, username, name, profile, consult_duration_total,consult_cnt_total" +
            ",consult_score_total,consult_cnt_today,consult_duration_today,age,id_card,phone,email,work_place,title" +
            " FROM user_counselor WHERE id = #{id}")
    Counselor getmassiveInfo(@Param("id")Integer id);

//    @Select("SELECT * FROM session WHERE counselor_id = #{id} and id = #{item}")
//    Session getSessionIfOk(Integer id, Integer item);
    //检查每一个输入的ID是否是本咨询师拥有的会话记录。
}
