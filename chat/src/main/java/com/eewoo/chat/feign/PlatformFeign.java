package com.eewoo.chat.feign;

import com.eewoo.common.pojo.vo.request.CounselorCommentRequest;
import com.eewoo.common.pojo.vo.request.SessionRequest;
import com.eewoo.common.pojo.vo.request.SessionSCRequest;
import com.eewoo.common.pojo.vo.request.VisitorCommentRequest;
import com.eewoo.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("platform-service")
public interface PlatformFeign {
    @PostMapping("/visitor/consult")
    R createSession(@RequestBody SessionRequest session, @RequestHeader String token);

    @PostMapping("visitor/comment")
    R giveCounselorComment(@RequestBody VisitorCommentRequest visitorCommentRequest,  @RequestHeader String token);

    @PostMapping("/counselor/comment")
    R giveVisitorComment(@RequestBody CounselorCommentRequest counselorCommentRequest,  @RequestHeader String token);

    @GetMapping("/counselor/get-supervisor")
    R getSupervisor(@RequestHeader String token);

    @PostMapping("/counselor/consult")
    R createSCSession(@RequestBody SessionSCRequest sessionRequest, @RequestHeader String token);

    @GetMapping("/supervisor/end-sc-session")
    R endSCSession(@RequestParam Integer sessionId, @RequestHeader String token);
}
