package com.eewoo.chat.controller;

import com.eewoo.chat.pojo.Chat;
import com.eewoo.chat.pojo.CounselorComment;
import com.eewoo.chat.pojo.VisitorComment;
import com.eewoo.chat.service.ChatService;
import com.eewoo.chat.service.FileService;
import com.eewoo.common.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    ChatService chatService;
    @Autowired
    FileService fileService;

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

    /**
     * 某个咨询师查看自己当前正在会话数
     * @return
     */
    @PreAuthorize("hasAuthority('c')")
    @GetMapping("current-chats")
    public R currentChatsNum(){
        Integer chatsNum = chatService.getChatsNum();
        return R.ok(chatsNum);
    }

    /**
     * 获取在线的咨询师的id 给访客服务调用
     * @return
     */
    @GetMapping("online-counselors")
    public R getOnlineCounselorIds(){
        List<Integer> results = chatService.getOnlineCounselors();
        return R.ok(results);
    }

    @GetMapping("/file/session")
    public void getSession(@RequestParam Integer sessionId, HttpServletResponse response) {
        chatService.getSessionInMongo(sessionId, response);
    }

    @GetMapping("/file/sessions")
    public void getSessions(@RequestParam List<Integer> sessionIds, HttpServletResponse response){
        chatService.getSessionsInMongo(sessionIds, response);
    }


    /**
     * 获取某一次的聊天
     * @param sessionId
     * @return
     */
    @GetMapping("/session-content")
    public R getSessionContent(@RequestParam Integer sessionId){
        Chat chat = chatService.getSessionContent(sessionId);
        return R.ok(chat);
    }


    @PostMapping("/file/upload")
    public R uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String path = fileService.uploadFile(file);
        if (path != null) {
            return R.ok(path);
        }
        return R.err("上传失败哦");
    }

    @GetMapping("/file/get/{filename}")
    public void getFile(@PathVariable String filename, HttpServletResponse response) {
        fileService.getFileStream(filename, response);
    }


}
