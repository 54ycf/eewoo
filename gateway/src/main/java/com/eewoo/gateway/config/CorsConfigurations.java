package com.eewoo.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfigurations {


    /**
     * 首先我们配置一个CorsWebFilter
     * 然后发先需要配置源
     * 找出它的实现进行new
     * 我们接着注册配置, 发现需要CorsConfiguration
     * 继续new出来, 最后进行配置
     *
     * @return
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*"); // 允许任何头
        corsConfiguration.addAllowedMethod("*"); // 允许任何方法
        corsConfiguration.setAllowedOrigins(Arrays.asList("*")); // 允许任何来源
//        corsConfiguration.setAllowedOriginPatterns();
        corsConfiguration.setAllowCredentials(true); // 允许携带cookie
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(source);
    }
}
