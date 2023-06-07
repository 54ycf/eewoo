package com.eewoo.platform.controller;

import com.eewoo.common.pojo.ScheduleCounselor;
import com.eewoo.common.pojo.Visitor;
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

import javax.servlet.http.HttpServletRequest;

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


    @GetMapping("/consul_records")
    public R getSessions(){
        List<SessionResponse> sessionResponses=adminService.getSessions();
        return R.ok(sessionResponses);
    }

    @GetMapping("/consultant")
    public R getCounselors(){
        List<CounselorSupervisorResponse> counselorSupervisorResponses= adminService.getCounselors();
        return R.ok(counselorSupervisorResponses);
    }

    @GetMapping("/supervisor")
    public R getSupervisors(){
        List<CounselorSupervisorResponse> counselorSupervisorResponses= adminService.getSupervisors();
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

    @GetMapping("/visitor")
    public R getVisitors(){
        List<VisitorResponse> visitorResponses=adminService.getVistors();
        return R.ok(visitorResponses);
    }

    @GetMapping("/top_session")
    public R getTopSessionCounselors(){
        List<CounselorResponse> counselorResponses=adminService.getTopSessions();
        return R.ok(counselorResponses);
    }

    @GetMapping("/top_score")
    public R getTopScoreCounselors(){
        List<CounselorResponse> counselorResponses= adminService.getTopScoreCounselors();
        return R.ok(counselorResponses);
    }

    @GetMapping("/schedule/counselor")
    public R getCounselorSchedule() {
        List<ScheduleCounselorResponse> scheduleCounselors= adminService.getCounselorSchedules();
        return R.ok(scheduleCounselors);
    }

    @GetMapping("/schedule/supervisor")
    public R getSupervisorSchedule() {
        List<ScheduleSupervisorResponse> scheduleSupervisorResponses= adminService.getSupervisorSchedules();
        return R.ok(scheduleSupervisorResponses);
    }

    //给咨询师排班
    @PutMapping("/schedule/counselor/add")
    public R putCounselorSchedule(@RequestBody ScheduleCounselorRequest scheduleCounselorRequest){
        int ok=adminService.putCounselorSchedule(scheduleCounselorRequest);
        if(ok>0) {
            return R.ok("排班成功！");
        }
        return R.err("-1","排班失败！");
    }

    @PutMapping("/schedule/counselor/delete")
    public R removeCounselorSchedule(@RequestBody ScheduleCounselorRequest scheduleCounselorRequest){
        int ok=adminService.removeCounselorSchedule(scheduleCounselorRequest);
        if(ok>0){
            return R.ok("移除成功！");
        }
        return R.err("-1","移除失败！");
    }

    @PutMapping("/schedule/supervisor/delete")
    public R removeCounselorSchedule(@RequestBody ScheduleSupervisorRequest scheduleSupervisorRequest){
        int ok=adminService.removeSupervisorSchedule(scheduleSupervisorRequest);
        if(ok>0){
            return R.ok("移除成功！");
        }
        return R.err("-1","移除失败！");
    }



}