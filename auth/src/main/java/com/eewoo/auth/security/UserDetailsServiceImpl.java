package com.eewoo.auth.security;

import com.eewoo.auth.mapper.UserMapper;
import com.eewoo.common.pojo.User;
import com.eewoo.common.security.LoginUser;
import com.eewoo.common.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 创建一个类实现UserDetailsService接口，重写其中的方法。更加用户名从数据库中查询用户信息
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String role = username.substring(0,1);
        //根据用户名和db查询用户信息
        String table = Constant.roleTableMap.get(role);
        User admin = userMapper.selectUserByNameAndTable(username.substring(2), table);
        if (null == admin){
            throw new RuntimeException("用户不存在");
        }
        admin.setRole(role);
        //如果查询不到数据就通过抛出异常来给出提示
        //测试写法
        List<String> list = new ArrayList<>(Collections.singletonList(role));
        //封装成UserDetails对象返回
        return new LoginUser(admin, list);
    }


}