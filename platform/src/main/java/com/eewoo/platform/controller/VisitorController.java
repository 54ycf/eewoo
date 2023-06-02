package com.eewoo.platform.controller;

import com.eewoo.common.pojo.Session;
import com.eewoo.common.util.R;
import com.eewoo.platform.pojo.vo.request.CommentRequest;
import com.eewoo.platform.pojo.vo.response.CounselorResponse;
import com.eewoo.platform.pojo.vo.response.SessionResponse;
import com.eewoo.platform.pojo.vo.response.VisitorResponse;
import com.eewoo.platform.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasAuthority('v')")
@RestController
@RequestMapping("/visitor")
public class VisitorController {
    @Autowired
    VisitorService visitorService;


    @GetMapping("/info")
    public R getInfo() {
        VisitorResponse info = visitorService.getInfo();
        if (info==null)
            return R.err("无此访客");
        return R.ok(info);
    }

    @GetMapping("/consultants")
    public R getCounselors(){
        List<CounselorResponse> counselorResponses= visitorService.getCounselors();
        return R.ok(counselorResponses);
    }

    @GetMapping("/consultants/history")
    public R getHistoryCounselors(){
        List<CounselorResponse> counselorResponses= visitorService.getHistoryCounselors();
        return R.ok(counselorResponses);
    }

    @PostMapping("/comment")
    public R giveCounselorComment(@RequestBody CommentRequest commentRequest){
        visitorService.giveCounselorComment(commentRequest.getSessionId(),
                commentRequest.getVisitorFeedback(),
                commentRequest.getVisitorFeedbackScore());
        return R.ok();
    }

    @PostMapping("/consult")
    public R createSession(@RequestBody Session session){
        int id=visitorService.createSession(session);
        if(id>0){
            return R.ok(id);
        }
        return R.err("-1","创建会话失败！");
    }

    @GetMapping("/consults")
    public R getHistorySessions(){
        List<SessionResponse> sessionResponses= visitorService.getHistorySessions();
        return R.ok(sessionResponses);
    }








}
