package com.eewoo.platform.controller;

import com.eewoo.common.pojo.Counselor;
import com.eewoo.common.pojo.Supervisor;
import com.eewoo.common.util.R;
import com.eewoo.platform.pojo.RoughCouselor;
import com.eewoo.platform.pojo.vo.response.*;
import com.eewoo.platform.pojo.vo.request.FindCounselorMsg;
import com.eewoo.platform.service.SupervisorService;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@PreAuthorize("hasAuthority('s')")
@RestController
@RequestMapping("/supervisor")
public class SupervisorController {

    @Autowired
    SupervisorService superservice;


    /**获得督导登陆之后除密码外的所有个人存储字段*/
    @GetMapping("/supervisor-info")
    public R getSupervisorInfo() {
        Supervisor sp = superservice.getSupervisorInfo();
        if(sp != null)
        {
            return R.ok(sp);
        }
        else
            return R.err("500","服务器内部错误");
    }

    /**
     * 获得访客咨询的列表。
     * 抛弃抛弃抛弃抛弃抛弃抛弃抛弃抛弃抛弃抛弃抛弃抛弃抛弃抛弃抛弃抛弃抛弃抛弃抛弃
     * 督导室不能直接看见访客的信息的
     * 督导只接受来自咨询师的邀请和管理员分配的信息
     * @return
     */
    @GetMapping("/visitor-list")
    public R getvisitorList() {
        List<VisitorResponse> visitor_list = superservice.getVisitorList();
        if(visitor_list.size() > 0)
            return R.ok(visitor_list);
        else
            return R.err("500","未找到任何访客记录");
    }

    /**
     * 获得与本督导具有绑定关系的咨询师的列表 and status。page和size用来分页。
     * 分页功能已经调通，按照这个模板来调其他的分页功能
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/counsult-record/table")
    public R bindconuselors(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page, @RequestParam(name = "size", required = false, defaultValue = "20") Integer size)
    {
        PageInfo<BindCounselorResponse> bindCounselorResponsePageInfo = superservice.bindCounselorsList(page, size);
        return R.ok(bindCounselorResponsePageInfo);
    }

    /**
     * 得到和自己有聊天记录的咨询师的一个大致的左侧边列表，包含咨询师的名字和邮箱就可以了，
     * 还要咨询师的ID，比较好做进一步的再次请求后端查看详细chat信息，那部分是chat服务负责了。
     * //遇到问题，我们并没有存放counselor和supervissor的表格，先随机返回吧。
     * @return 粗略的最近有聊天记录的咨询师
     * 废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃
     */
    @GetMapping("/counselor-chat-list")
    public R leftBar()
    {
        List<RoughCouselor> list = superservice.getLatelyChatCounselors();
        if(list != null)
            return R.ok(list);
        return R.err("500","服务器里边儿没有");
    }


    @PostMapping( "/search-consultant" )
    public R findCounselor(@RequestBody FindCounselorMsg fcm)
    {

        if(fcm != null)
        {
            List<Counselor> cons = superservice.findCounselors(fcm);
            if(cons.size() > 0)
                return R.ok(cons);
            return R.err("400","未找到资源");

        }
        else
            return R.err("500","SERVER　ＥＲＲＯＲ");

    }

    /**获取supervisor的排班(按日期 day)
     * 和管理员获取的督导排班表不同，这里只是获取本督导一个人的排班表
     * 对于前端来讲渲染的逻辑是相同的。
     * **/
    @GetMapping("/working-schedule/day")
    public R getWorkingScheduleByDay()
    {
        List<DayScheduleSupervisorResponse> dayScheduleSupervisorRespons = superservice.getSuperVisorSchedulesByDay();
        return R.ok(dayScheduleSupervisorRespons);
    }

    /**获取supervisor的排班(按星期 week)
     * 和管理员获取的督导排班表不同，这里只是获取本督导一个人的排班表
     * 对于前端来讲渲染的逻辑是相同的。
     * **/
    @GetMapping("/working-schedule/week")
    public R getWorkingScheduleByWeek()
    {
        List<ScheduleSupervisorResponse> scheduleSupervisorResponses= superservice.getSupervisorSchedules();
        return R.ok(scheduleSupervisorResponses);
    }
}
