package com.eewoo.platform.controller;

import com.eewoo.common.pojo.vo.request.CounselorCommentRequest;
import com.eewoo.common.pojo.Counselor;
import com.eewoo.common.pojo.User;
import com.eewoo.common.pojo.vo.request.SessionSCRequest;
import com.eewoo.common.util.R;
import com.eewoo.platform.pojo.vo.request.DayScheduleCounselorRequest;
import com.eewoo.platform.pojo.vo.request.c_Evaluation;
import com.eewoo.platform.pojo.vo.response.*;
import com.eewoo.platform.service.CounselorService;
import com.eewoo.platform.utils.ZipUtils;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.UrlResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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


    /**
     * counselor and supervisor session creation
     * all staff maninupulation here
     */

    @GetMapping("/get-supervisor")
    R getSupervisor()
    {
        return R.ok(counselorService.getBindSupervisor());
    }

    @PostMapping("/consult")
    R createSCSession(@RequestBody SessionSCRequest sessionRequest)
    {
        Integer sessionId = counselorService.createSCSessionAndFetchID(sessionRequest);
        return R.ok(sessionId);
    }

    /**
     * 获取除密码外的所有字段
     * 咨询师的个人信息
     * @return
     */
    @GetMapping("/counselor-info")
    public R getCounselorInfo()
    {
        Counselor this_conselor = counselorService.getAllInfoExceptPassword();
        if(this_conselor != null)
            return R.ok(this_conselor);
        else
            return R.err("500","Error");
    }

    /**
     * 查询咨询记录列表，涉及到分页操作，前端传来page和size
     * 访客和咨询师对话的概览界面展示
     * @return
     */
    @GetMapping("/consult-list")
    public R getConsultList(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                            @RequestParam(name = "size", required = false, defaultValue = "20") Integer size)
    {
        PageInfo<Consult> list = counselorService.getConsult(page, size);
        if(list!= null)
        {
            if(list.getList().size() >0)
                return R.ok(list);
            else //没有大小说明没有人
                return R.err("200","暂无已经咨询的用户");
        }
        else//为空说明出错了
            return R.err("code =500", "error");
    }

    @GetMapping("/get-consult-list")
    public R getRealConsultList(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                @RequestParam(name = "size", required = false, defaultValue = "20") Integer size,
                                @RequestParam(name = "name", required = false) String name,
                                @RequestParam(name = "date", required = false) String date)
    {
        PageInfo<SessionResponse> sessionResponses=counselorService.getCounselorRelatedSession(page,size,name,date);
        return R.ok(sessionResponses);
    }
    /**
     * 获得总的咨询时长
     * @return
     */
    @GetMapping("/total-consult")
    public R getTotalConsultTime()
    {

        Integer time = counselorService.getTotalSessionTime();
        if(time != null)
            return R.ok(time);
        else
            return R.err("code =500", "error");
    }

    /**
     * 获得今日咨询数量
     * @return
     */
    @GetMapping("/today-consults")
    public R getTodayConsultsNum() {
        Integer time = counselorService.getTodaySessionNum();
        if (time != null)
            return R.ok(time);
        else
            return R.ok(0);
    }

    /**
     * 获得今日咨询总时长
     * @return
     */
    @GetMapping("/today-total-time")
    public R getTodayConsultsTime() {
        Integer time = counselorService.getTodaySessionTime();
        if (time != null)
        {
            return R.ok(time);
        }
        else
            return R.ok(0);
    }

    /*****
     * 当前会话总数功能在chat那里，需要提供service接口。
     * 由chat的service负责
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


    /**获取counselor的排班(按日期 day)
     * 和管理员获取的咨询师排班表不同，这里只是获取本咨询师一个人的排班表
     * 对于前端来讲渲染的逻辑是相同的。
     * **/
    @GetMapping("/working-schedule/day")
    public R getWorkingScheduleByDay()
    {
        List<DayScheduleCounselorResponse> dayScheduleSupervisorRespons = counselorService.getSuperVisorSchedulesByDay();
        return R.ok(dayScheduleSupervisorRespons);
    }

    /**获取counselor的排班(按星期 week)
     * 和管理员获取的咨询师排班表不同，这里只是获取本咨询师一个人的排班表
     * 对于前端来讲渲染的逻辑是相同的。
     * 就算是weekday返回的也是一个list，可能一个人也有多个星期数，不能只返回一个星期数
     * **/
    @GetMapping("/working-schedule/week")
    public R getWorkingScheduleByWeek()
    {
        List<ScheduleCounselorResponse> scheduleSupervisorResponses= counselorService.getCounselorSchedules();
        return R.ok(scheduleSupervisorResponses);
    }

}

/**
 * 这两个任务在CounselorServiceImpl供Chat服务模块调用，不在本Controller做路由
 * 1.咨询师填写会话评价内容
 * 2.查找督导
 */