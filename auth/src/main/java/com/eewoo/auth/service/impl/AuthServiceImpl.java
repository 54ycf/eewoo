package com.eewoo.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.eewoo.common.security.LoginUser;
import com.eewoo.auth.service.AuthService;
import com.eewoo.auth.util.JwtUtil;
import com.eewoo.auth.util.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private RedisCache redisCache;

    @Override
    public LoginUser parse(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        //解析token
        String userId;
        String role;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String userIDAndRole = claims.getSubject();
            Map<String, String> map = (Map<String, String>) JSON.parse(userIDAndRole);
            userId = map.get("userId");
            role = map.get("role");
        } catch (Exception e) {
            e.printStackTrace(); //token解析失败
            throw new RuntimeException("token非法");
        }
        //从redis中获取用户信息
        String redisKey = "login:"+role+":"+userId;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        if(Objects.isNull(loginUser)){
            throw new RuntimeException("用户未登录");
        }
        return loginUser;
    }
}
