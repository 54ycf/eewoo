package com.eewoo.chat.controller;

import com.eewoo.chat.pojo.CounselorComment;
import com.eewoo.chat.pojo.VisitorComment;
import com.eewoo.chat.service.ChatService;
import com.eewoo.common.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    ChatService chatService;

    /**
     * 访客发起会话
     * @param counselorId
     * @return
     */
    @PreAuthorize("hasAuthority('v')")
    @GetMapping("/call-counselor")
    public R callCounselor(@RequestParam Integer counselorId, @RequestParam String counselorName){
        if (chatService.callCounselor(counselorId, counselorName)) {
            return R.ok("会话开启成功");
        }
        return R.err("咨询师忙碌，请等待");
    }

    /**
     * 访客或者咨询师结束会话
     * @param chatToken
     * @return
     */
    @PreAuthorize("hasAnyAuthority('v','c')")
    @GetMapping("/end-vc-session")
    public R endVCSession(@RequestParam String chatToken){
        chatService.endVCSession(chatToken);
        return R.ok();
    }

    /**
     * 访客对咨询师发起评价
     * @return
     */
    @PreAuthorize("hasAuthority('v')")
    @PostMapping("/visitor-comment")
    public R visitorComment(@RequestBody VisitorComment visitorComment){
        chatService.visitorComment(visitorComment);
        return R.ok();
    }

    /**
     * 咨询师对访客此次会话发起评价
     * @param counselorComment
     * @return
     */
    @PreAuthorize("hasAuthority('c')")
    @PostMapping("/counselor-comment")
    public R counselorComment(@RequestBody CounselorComment counselorComment){
        chatService.counselorComment(counselorComment);
        return R.ok();
    }


    /**
     * 咨询师联系督导
     * @param chatToken 是访客发送给咨询师的chatToken！
     * @return
     */
    @GetMapping("call-supervisor")
    public R callSupervisor(@RequestParam String chatToken){
        if (!chatService.callSupervisor(chatToken)) {
            return R.err("无督导在线");
        }
        return R.ok();
    }

    /**
     * 咨询师或者督导结束会话，直接结束
     * @param chatToken
     * @return
     */
    @GetMapping("end-cs-session")
    public R endCSSession(@RequestParam String chatToken){
        chatService.endCSSession(chatToken);
        return R.ok();
    }

}
