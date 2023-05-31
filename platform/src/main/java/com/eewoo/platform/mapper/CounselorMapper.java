package com.eewoo.platform.mapper;

import com.eewoo.common.pojo.Supervisor;
import com.eewoo.platform.pojo.vo.response.Consult;
import org.apache.ibatis.annotations.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

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


    @Select("SELECT name, start_time, end_time, duration FROM session join user_visitor on id WHERE counselor_id = #{id}")
    List<Consult> getAllConsults(@Param("id") Integer counselorId);
}
