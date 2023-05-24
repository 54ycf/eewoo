package com.eewoo.platform.mapper;

import com.eewoo.common.pojo.Visitor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface VisitorMapper {
    @Select("select * from user_visitor where id = #{id}")
    Visitor getInfo(Integer id);
}
