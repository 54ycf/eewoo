package com.eewoo.platform.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * 涉及到用户到很笼统到操作，比如禁用用户，四个角色都有这个功能，可以写到User里
 */
@Mapper
@Repository
public interface UserMapper{
    @Update("UPDATE ${table} SET banned = 1 WHERE id = #{id}")
    int disableUser(@Param("id") Integer id, @Param("table") String table);

    @Update("UPDATE ${table} SET banned = 0 WHERE id = #{id}")
    int enableUser(@Param("id") Integer id, @Param("table") String s);
}
