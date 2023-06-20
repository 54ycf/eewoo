package com.eewoo.platform.feign;

import com.eewoo.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("chat-service")
public interface ChatFeign {
    @GetMapping("/chat/online-counselors")
     R getOnlineCounselorIds(@RequestHeader String token);
}
