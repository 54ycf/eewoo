package com.eewoo.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.eewoo.auth.mapper.UserMapper;
import com.eewoo.common.security.LoginUser;
import com.eewoo.auth.service.AccountService;
import com.eewoo.auth.util.JwtUtil;
import com.eewoo.auth.util.RedisCache;
import com.eewoo.common.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private UserMapper userMapper;


    @Override
    public String login(User user) {
        System.out.println("visited ！！");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getRole()+"-"+user.getUsername(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String role = loginUser.getUser().getRole();
        Map<String, String> map = new HashMap<String, String>(){{put("userId",userId);put("role",role);}};
        String jsonMapStr = JSON.toJSONString(map);
        String jwt = JwtUtil.createJWT(jsonMapStr);
        //authenticate存入redis
        redisCache.setCacheObject("login:" + role + ":" +userId, loginUser);
        //把token响应给前端
        return jwt;
    }

    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Integer userid = loginUser.getUser().getId();
        String role = loginUser.getUser().getRole();
        redisCache.deleteObject("login:"+role+":"+userid);
    }

    @Override
    public void logout(Integer id, String role) {
        redisCache.deleteObject("login:"+role+":"+id);
    }

    @Override
    public int registerVisitor(Visitor visitor) {
        if (null != userMapper.selectUserByNameAndTable(visitor.getUsername(), "user_visitor"))
            return -1;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        visitor.setPassword(encoder.encode(visitor.getPassword()));
        return userMapper.addVisitor(visitor);
    }

    @Override
    public int addCounselor(Counselor counselor) {
        if (null != userMapper.selectUserByNameAndTable(counselor.getUsername(), "user_counselor"))
            return -1;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        counselor.setPassword(encoder.encode(counselor.getPassword()));
        return userMapper.addCounselor(counselor);
    }

    @Override
    public int addSupervisor(Supervisor supervisor) {
        if (null != userMapper.selectUserByNameAndTable(supervisor.getUsername(), "user_supervisor"))
            return -1;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        supervisor.setPassword(encoder.encode(supervisor.getPassword()));
        return userMapper.addSupervisor(supervisor);
    }

}