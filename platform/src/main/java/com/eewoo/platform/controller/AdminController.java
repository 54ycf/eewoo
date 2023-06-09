package com.eewoo.platform.controller;

import com.eewoo.platform.pojo.vo.request.BindRequest;
import com.eewoo.platform.pojo.vo.request.DisableUserRequest;
import com.eewoo.common.util.R;
import com.eewoo.platform.feign.AuthFeign;
import com.eewoo.platform.pojo.vo.request.ScheduleCounselorRequest;
import com.eewoo.platform.pojo.vo.request.ScheduleSupervisorRequest;
import com.eewoo.platform.pojo.vo.response.*;
import com.eewoo.platform.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@PreAuthorize("hasAuthority('a')")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AuthFeign authFeign;
    @Autowired
    AdminService adminService;

    String getToken(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("token");
    }

    /**
     * 禁用用户，MySQL字段更新，auth-service的缓存清除
     * @param disableUser
     * @return
     */
    @PutMapping("/disable")
    public R disableUser(@RequestBody DisableUserRequest disableUser){
        authFeign.logout(disableUser.getId(), disableUser.getRole(), getToken()); //远程清除redis缓存
        adminService.disableUser(disableUser.getId(), disableUser.getRole());
        return R.ok();
    }

    @PutMapping("/enable")
    public R enableUser(@RequestBody DisableUserRequest enableUser){
        adminService.enableUser(enableUser.getId(), enableUser.getRole());
        return R.ok();
    }

    @GetMapping("/consul_records")
    public R getSessions(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page, @RequestParam(name = "size", required = false, defaultValue = "20") Integer size){
        List<SessionResponse> sessionResponses=adminService.getSessions(page,size);
        return R.ok(sessionResponses);
    }

    /**获取咨询师**/
    @GetMapping("/consultant")
    public R getCounselors(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page, @RequestParam(name = "size", required = false, defaultValue = "20") Integer size){
        List<CounselorSupervisorResponse> counselorSupervisorResponses= adminService.getCounselors(page, size);
        return R.ok(counselorSupervisorResponses);
    }

    /**获取督导**/
    @GetMapping("/supervisor")
    public R getSupervisors(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page, @RequestParam(name = "size", required = false, defaultValue = "20") Integer size){
        List<CounselorSupervisorResponse> counselorSupervisorResponses= adminService.getSupervisors(page, size);
        return R.ok(counselorSupervisorResponses);
    }

    @DeleteMapping("/consultant/{id}")
    public R removeCounselor(@PathVariable("id")Integer id){
        int ok=adminService.removeCounselor(id);
        if(ok>0){
            return R.ok();
        }
        return R.err("-1","没有找到要删除的数据");
    }

    /**获取访客**/
    @GetMapping("/visitor")
    public R getVisitors(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page, @RequestParam(name = "size", required = false, defaultValue = "20") Integer size){
        List<VisitorResponse> visitorResponses=adminService.getVistors(page, size);
        return R.ok(visitorResponses);
    }

    /**获取会话数最多的咨询师**/
    @GetMapping("/top_session")
    public R getTopSessionCounselors(){
        List<CounselorResponse> counselorResponses=adminService.getTopSessions();
        return R.ok(counselorResponses);
    }

    /**获取评分最高的咨询师**/
    @GetMapping("/top_score")
    public R getTopScoreCounselors(){
        List<CounselorResponse> counselorResponses= adminService.getTopScoreCounselors();
        return R.ok(counselorResponses);
    }

    /**获取咨询师排班表(按星期)**/
    @GetMapping("/schedule/week/counselor")
    public R getCounselorScheduleByWeek() {
        List<ScheduleCounselorResponse> scheduleCounselors= adminService.getCounselorSchedules();
        return R.ok(scheduleCounselors);
    }

    /**获取督导排班表(按星期)**/
    @GetMapping("/schedule/week/supervisor")
    public R getSupervisorScheduleByWeek() {
        List<ScheduleSupervisorResponse> scheduleSupervisorResponses= adminService.getSupervisorSchedules();
        return R.ok(scheduleSupervisorResponses);
    }

    /**添加咨询师排班(按星期)**/
    @PutMapping("/schedule/week/counselor/add")
    public R putCounselorScheduleByWeek(@RequestBody ScheduleCounselorRequest scheduleCounselorRequest){
        int ok=adminService.putCounselorSchedule(scheduleCounselorRequest);
        if(ok>0) {
            return R.ok("排班成功！");
        }
        return R.err("-1","排班失败！");
    }

    /**删除咨询师排班(按星期)**/
    @PutMapping("/schedule/week/counselor/delete")
    public R removeCounselorScheduleByWeek(@RequestBody ScheduleCounselorRequest scheduleCounselorRequest){
        int ok=adminService.removeCounselorSchedule(scheduleCounselorRequest);
        if(ok>0){
            return R.ok("移除成功！");
        }
        return R.err("-1","移除失败！");
    }

    /**删除督导排班(按星期)**/
    @PutMapping("/schedule/week/supervisor/delete")
    public R removeCounselorScheduleByWeek(@RequestBody ScheduleSupervisorRequest scheduleSupervisorRequest){
        int ok=adminService.removeSupervisorSchedule(scheduleSupervisorRequest);
        if(ok>0){
            return R.ok("移除成功！");
        }
        return R.err("-1","移除失败！");
    }

    /**获取咨询师的排班(按日期)**/
    @GetMapping("/schedule/day/counselor")
    public R getCounselorScheduleByDay(){
        List<DayScheduleCounselorResponse> dayScheduleCounselorRespons = adminService.getCounselorSchedulesByDay();
        return R.ok(dayScheduleCounselorRespons);
    }

    /**获取督导的排班(按日期)**/
    @GetMapping("/schedule/day/supervisor")
    public R getSupervisorScheduleByDay(){
        List<DayScheduleSupervisorResponse> dayScheduleSupervisorResponses= adminService.getSupervisorSchedulesByDay();
        return R.ok(dayScheduleSupervisorResponses);
    }

    /**添加咨询师排班(按日期)**/
    @GetMapping("/schedule/day/counselor/add")
    public R putCounselorScheduleByDay(){
        return R.ok();
    }

    /**查看某一个具体的咨询师**/
    @GetMapping("/admin/week/consultant/detail")
    public R getCounselorDetail(@RequestParam(name = "counselorId", required = true) Integer counselorId ){
        CounselorResponse counselorResponse=adminService.getCounselorById(counselorId);
        return R.ok(counselorResponse);
    }

    @GetMapping("/admin/consultant/search")
    public R getCounselorByName(@RequestParam(name = "name",required = false,defaultValue = "")String name){
        List<CounselorResponse> counselorResponses=null;
        if(name.length()<1){
            counselorResponses= adminService.getCounselorsWithoutSupervi(1,20);
            return R.ok(counselorResponses);
        }
        counselorResponses= adminService.getCounselorByName(name);
        return R.ok(counselorResponses);
    }

    /**修改咨询师和督导的绑定关系**/
    @PutMapping("/admin/consultant/bind")
    public R reviseBind(@RequestBody BindRequest bindRequest){
        int ok=adminService.reviseBind(bindRequest);
        if(ok>0){
            R.ok("绑定成功！");
        }
        return R.err("-1","绑定失败！");
    }








}