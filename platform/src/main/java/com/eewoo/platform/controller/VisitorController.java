package com.eewoo.platform.controller;

import com.eewoo.common.pojo.Counselor;
import com.eewoo.common.pojo.vo.request.SessionRequest;
import com.eewoo.common.util.R;
import com.eewoo.common.pojo.vo.request.VisitorCommentRequest;
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

    /**获取访客信息**/
    @GetMapping("/info")
    public R getInfo() {
        VisitorResponse info = visitorService.getInfo();
        if (info==null)
            return R.err("无此访客");
        return R.ok(info);
    }

    /**获取所有咨询师列表**/
    @GetMapping("/consultants")
    public R getCounselors(){
        List<CounselorResponse> counselors= visitorService.getCounselors();
        return R.ok(counselors);
    }

    /**获取历史咨询师列表**/
    @GetMapping("/consultants/history")
    public R getHistoryCounselors(){
        List<CounselorResponse> counselorResponses= visitorService.getHistoryCounselors();
        return R.ok(counselorResponses);
    }

    /**给咨询师评论**/
    @PostMapping("/comment")
    public R giveCounselorComment(@RequestBody VisitorCommentRequest visitorCommentRequest){
        visitorService.giveCounselorComment(visitorCommentRequest.getSessionId(),
                visitorCommentRequest.getVisitorFeedback(),
                visitorCommentRequest.getVisitorFeedbackScore());
        return R.ok();
    }

    /**和咨询师创建会话**/
    @PostMapping("/consult")
    public R createSession(@RequestBody SessionRequest session){
        int id=visitorService.createSession(session);
        if(id>0){
            return R.ok(id);
        }
        return R.err("-1","创建会话失败！");
    }

    /**获取历史咨询记录**/
    @GetMapping("/consults")
    public R getHistorySessions(){
        List<SessionResponse> sessionResponses= visitorService.getHistorySessions();
        return R.ok(sessionResponses);
    }





}
