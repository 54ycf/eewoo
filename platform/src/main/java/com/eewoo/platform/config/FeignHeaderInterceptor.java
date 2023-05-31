//package com.eewoo.platform.config;
//
//import feign.RequestInterceptor;
//import feign.RequestTemplate;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Enumeration;
//
///**
// * 默认Feign的时候自动转发header
// */
//@Configuration
//public class FeignHeaderInterceptor implements RequestInterceptor {
//
//    @Override
//    public void apply(RequestTemplate template) {
//        // 获取当前请求的 HttpServletRequest
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//
//        // 将 HttpServletRequest 的头部信息转发到 Feign 请求的头部
//        Enumeration<String> headerNames = request.getHeaderNames();
//        if (headerNames != null) {
//            while (headerNames.hasMoreElements()) {
//                String headerName = headerNames.nextElement();
//                String headerValue = request.getHeader(headerName);
//                template.header(headerName, headerValue);
//            }
//        }
//    }
//}
