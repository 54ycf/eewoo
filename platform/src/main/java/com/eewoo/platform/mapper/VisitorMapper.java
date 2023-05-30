package com.eewoo.platform.mapper;

import com.eewoo.common.pojo.Counselor;
import com.eewoo.common.pojo.Session;
import com.eewoo.common.pojo.Visitor;
import com.eewoo.platform.pojo.vo.request.SessionRequest;
import com.eewoo.platform.pojo.vo.response.SessionResponse;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface VisitorMapper {
    @Select("select * from user_visitor where id = #{id}")
    Visitor getInfo(Integer id);

    @Select("select * from user_counselor")
    List<Counselor> getCounselors();

    @Select("Select * from session where visitor_id = #{id}")
    List<SessionResponse> getSessions(Integer id);

    @Select("Select * from user_counselor where id = #{id}")
    Counselor getHistoryCounselor(Integer id);

    @Update("Update session set visitor_feedback=#{feedback},visitor_feedback_score=#{score} where id=#{id}")
    void insertComment(Integer id,String feedback,Integer score);

    @Insert("INSERT INTO session (visitor_id,counselor_id,start_time,end_time,duration,visitor_feedback,visitor_feedback_score,counselor_feedback,type)" +
            " VALUES (#{visitorId},#{counselorId},#{startTime},#{endTime},#{duration},#{visitorFeedback},#{visitorFeedbackScore},#{counselorFeedback},#{type})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int insertSession(Session session);

    @Select("Select * from session where visitor_id=#{visitorId}")
    List<Session> getHistorySessions(Integer visitorId);

}
