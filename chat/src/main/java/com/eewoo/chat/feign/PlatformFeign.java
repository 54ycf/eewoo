package com.eewoo.chat.feign;

import com.eewoo.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("platform-service")
public interface PlatformFeign {
    @GetMapping("/visitor/make-session")
    public R makeSession(@RequestHeader String token);
}
