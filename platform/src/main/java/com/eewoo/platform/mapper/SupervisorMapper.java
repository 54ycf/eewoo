package com.eewoo.platform.mapper;

import com.eewoo.platform.pojo.vo.response.BindCounselorResponse;
import com.eewoo.platform.pojo.vo.response.CounselorResponse;
import com.eewoo.platform.pojo.vo.response.VisitorResponse;
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


    @Select("SELECT user_counselor.name , session.duration,session.start_time FROM user_counselor join session ")
    List<BindCounselorResponse> getbindcounselors(  );

    @Select("SELECT name FROM user_supervisor WHERE id = #{id}")
    String getName(@Param("id") Integer id);


}
