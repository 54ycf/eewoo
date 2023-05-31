package com.eewoo.auth.mapper;

import com.eewoo.common.pojo.Counselor;
import com.eewoo.common.pojo.Supervisor;
import com.eewoo.common.pojo.User;
import com.eewoo.common.pojo.Visitor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    @Select("SELECT * FROM ${table} WHERE username = #{username} ")
    User selectUserByNameAndTable(String username, String table);

    @Select("SELECT * FROM ${table} WHERE username = #{username} AND banned = 0")
    User selectNormalUserByNameAndTable(String username, String table);

    @Insert("INSERT INTO user_visitor" +
            "(username,password,name,phone,emergency_person,emergency_contact) VALUES " +
            "(#{phone},#{password},#{name},#{phone},#{emergencyPerson},#{emergencyContact})")
    int addVisitor(Visitor visitor);

    @Insert("INSERT INTO user_counselor" +
            "(username,password,name,age,id_card,phone,email,work_place,title) VALUES " +
            "(#{username},#{password},#{name},#{age},#{idCard},#{phone},#{email},#{workPlace},#{title})")
    int addCounselor(Counselor counselor);

    @Insert("INSERT INTO user_supervisor" +
            "(username,password,name,age,id_card,phone,email,work_place,title,qualification,qualification_number) VALUES " +
            "(#{username},#{password},#{name},#{age},#{idCard},#{phone},#{email},#{workPlace},#{title},#{qualification},#{qualificationNumber})")
    int addSupervisor(Supervisor supervisor);


//    User test1(Integer id);
//
//    @Select("select * from user_admin where id = #{id}")
//    User test2(Integer id);
}