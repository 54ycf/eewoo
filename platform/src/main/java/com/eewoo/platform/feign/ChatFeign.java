package com.eewoo.platform.feign;

import com.eewoo.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient("chat-service")
public interface ChatFeign {
    @GetMapping("/chat/online-counselors")
     R<List<Integer>> getOnlineCounselorIds(@RequestHeader String token);
}
