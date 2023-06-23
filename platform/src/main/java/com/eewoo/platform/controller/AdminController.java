package com.eewoo.platform.controller;

import com.eewoo.platform.pojo.vo.request.*;
import com.eewoo.common.util.R;
import com.eewoo.platform.feign.AuthFeign;
import com.eewoo.platform.pojo.vo.response.*;
import com.eewoo.platform.service.AdminService;
import com.github.pagehelper.PageInfo;
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
        PageInfo<SessionResponse> sessionResponses=adminService.getSessions(page,size);
        return R.ok(sessionResponses);
    }

    /**获取督导**/
    @GetMapping("/supervisor/search")
    public R getSupervisors(@RequestParam(name = "name", required = false) String name,
                            @RequestParam(name = "page", required = false, defaultValue = "1")Integer page,
                            @RequestParam(name = "size", required = false, defaultValue = "20")Integer size){
        PageInfo<AdminSupervisorResponse> adminSupervisorResponses=null;
        PageInfo<AdminSupervisorResponse> supervisorList=adminService.getSupervisorList(page,size,name);
        return R.ok(supervisorList);
//        if(name==null || name.length()<1){
//            adminSupervisorResponses=adminService.getSupervisorsWithoutCounsel(page,size);
//            return R.ok(adminSupervisorResponses);
//        }
//        adminSupervisorResponses=adminService.getSupervisorByName(name,page,size);
//        return R.ok(adminSupervisorResponses);

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
        PageInfo<VisitorResponse> visitorResponses=adminService.getVistors(page, size);
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
    public R removeSupervisorScheduleByWeek(@RequestBody ScheduleSupervisorRequest scheduleSupervisorRequest){
        int ok=adminService.removeSupervisorSchedule(scheduleSupervisorRequest);
        if(ok>0){
            return R.ok("移除成功！");
        }
        return R.err("-1","移除失败！");
    }

    /**更新咨询师排班(按星期)**/
    @PutMapping("/schedule/week/counselor/update")
    public R updateCounselorScheduleByWeek(@RequestBody ScheduleCounselorUpdateRequest scheduleCounselorUpdateRequest){
        int ok=adminService.updateCounselorSchedule(scheduleCounselorUpdateRequest);
        if(ok>0){
            return R.ok("更新成功！");
        }
        return R.err("-1","更新失败！");
    }

    /**更新督导排班(按星期)**/
    @PutMapping("/schedule/week/supervisor/update")
    public R updateSupervisorScheduleByWeek(@RequestBody ScheduleSupervisorUpdateRequest scheduleSupervisorUpdateRequest){
        int ok=adminService.updateSupervisorSchedule(scheduleSupervisorUpdateRequest);
        if(ok>0){
            return R.ok("更新成功！");
        }
        return R.err("-1","更新失败！");
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
    @PutMapping("/schedule/day/counselor/add")
    public R putCounselorScheduleByDay(@RequestBody DayScheduleCounselorRequest dayScheduleCounselorRequest){
        int ok=adminService.putCounselorScheduleByDay(dayScheduleCounselorRequest);
        if(ok>0){
            return R.ok("添加成功！");
        }
        return R.err("-1","添加失败！");
    }

    /**移除咨询师的排班(按日期)**/
    @PutMapping("/schedule/day/counselor/delete")
    public R deleteCounselorScheduleByDay(@RequestBody DayScheduleCounselorRequest dayScheduleCounselorRequest){
        int ok=adminService.deleteCounselorScheduleByDay(dayScheduleCounselorRequest);
        if(ok>0){
            return R.ok("移除成功！");
        }
        return R.err("-1","移除失败！");
    }

    /**添加督导排班(按日期)**/
    @PutMapping("/schedule/day/supervisor/add")
    public R putSupervisorScheduleByDay(@RequestBody DayScheduleSupervisorRequest dayScheduleSupervisorRequest){
        int ok=adminService.putSupervisorScheduleByDay(dayScheduleSupervisorRequest);
        if(ok>0){
            return R.ok("添加成功！");
        }
        return R.err("-1","添加失败！");
    }

    /**移除督导排班(按日期)**/
    @PutMapping("/schedule/day/supervisor/delete")
    public R deleteSupervisorScheduleByDay(@RequestBody DayScheduleSupervisorRequest  dayScheduleSupervisorRequest){
        int ok=adminService.deleteSupervisorScheduleByDay(dayScheduleSupervisorRequest);
        if(ok>0){
            return R.ok("移除成功！");
        }
        return R.err("-1","移除失败！");
    }



    /**获取/按姓名搜索咨询师**/
    @GetMapping("/consultant/search")
    public R getCounselorByName(@RequestParam(name = "name", required = false) String name,
                                @RequestParam(name = "page", required = false, defaultValue = "1")Integer page,
                                @RequestParam(name = "size", required = false, defaultValue = "20")Integer size){
        PageInfo<AdminCounselorResponse> counselorList = adminService.getCounselorList(page, size, name);
        return R.ok(counselorList);
    }

    /**修改咨询师和督导的绑定关系**/
    @PutMapping("/consultant/bind")
    public R reviseBind(@RequestBody BindRequest bindRequest){
        int ok=adminService.reviseBind(bindRequest);
        System.out.println("ok"+ok);
        if(ok>0){
            return R.ok("绑定成功！");
        }
        return R.err("-1","绑定失败！");
    }








}