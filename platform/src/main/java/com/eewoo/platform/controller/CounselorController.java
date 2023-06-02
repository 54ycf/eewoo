package com.eewoo.platform.controller;

import com.eewoo.common.pojo.vo.request.CounselorCommentRequest;
import com.eewoo.common.util.R;
import com.eewoo.platform.pojo.vo.request.c_Evaluation;
import com.eewoo.platform.pojo.vo.response.Consult;
import com.eewoo.platform.service.CounselorService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasAuthority('c')")
@RestController
@RequestMapping("/counselor")
public class CounselorController {

    @Autowired
    CounselorService counselorService;
//    @PostMapping("/chat/counselor-comment")
//    public R evaluateOnSession(@RequestBody c_Evaluation eva)
//    {
//        counselorService.createEvaluation(eva.getSessionId(), eva.getCounselorFeedback(),eva.getType());
//        return R.ok();
//    }

    @GetMapping("/consult-list")
    public R getConsultList()
    {
        List<Consult> list = counselorService.getConsult();
        return R.ok(list);
        }

    /**
     * 以下任务在CounselorServiceImpl供Chat服务模块调用，不在本Controller做路由
     * 1.咨询师填写会话评价内容
     * 2.查找督导
     */

    /**
     * Feign-供chatService调用
     * 咨询师给访客的会话做评价
     * @param commentRequest 咨询师对访客（会话）的评价
     * @return
     */
    @PostMapping("/comment")
    public R giveVisitorComment(@RequestBody CounselorCommentRequest commentRequest){
        counselorService.createEvaluation(commentRequest.getSessionId(), commentRequest.getFeedback(), commentRequest.getType());
        return R.ok();
    }
}
