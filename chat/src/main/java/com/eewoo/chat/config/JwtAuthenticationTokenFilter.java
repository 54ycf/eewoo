package com.eewoo.chat.config;

import com.eewoo.chat.feign.AuthFeign;
import com.eewoo.common.security.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 我们需要自定义一个过滤器，这个过滤器会去获取请求头中的token，对token进行解析取出其中的userid。
 * 使用userid去redis中获取对应的LoginUser对象。
 * 然后封装Authentication对象存入SecurityContextHolder
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    AuthFeign authFeign;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //token为空先放行
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        LoginUser loginUser = authFeign.parseToken(token).getData();
        if(Objects.isNull(loginUser)){
            throw new RuntimeException("用户未登录");
        }
        loginUser.initAuthorities();

        //获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null, loginUser.getAuthorities());
        //存入SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}