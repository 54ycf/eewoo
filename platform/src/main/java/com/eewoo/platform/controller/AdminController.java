package com.eewoo.platform.controller;

import com.eewoo.platform.pojo.vo.request.DisableUserRequest;
import com.eewoo.common.util.R;
import com.eewoo.platform.feign.AuthFeign;
import com.eewoo.platform.pojo.vo.response.CounselorSupervisorResponse;
import com.eewoo.platform.pojo.vo.response.SessionResponse;
import com.eewoo.platform.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasAuthority('a')")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AuthFeign authFeign;
    @Autowired
    AdminService adminService;

    /**
     * 禁用用户，MySQL字段更新，auth-service的缓存清除
     * @param disableUser
     * @return
     */
    @PutMapping("/disable")
    public R disableUser(@RequestBody DisableUserRequest disableUser){
        authFeign.logout(disableUser.getId(), disableUser.getRole()); //远程清除redis缓存
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
}
