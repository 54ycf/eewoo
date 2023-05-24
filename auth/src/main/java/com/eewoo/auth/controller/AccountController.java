package com.eewoo.auth.controller;

import com.eewoo.auth.service.AccountService;
import com.eewoo.common.pojo.*;
import com.eewoo.common.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @PostMapping("/login")
    public R login(@RequestBody User user){
        String token = accountService.login(user);
        return R.ok(token);
    }

    @PutMapping("/logout")
    public R logout(){
        accountService.logout();
        return R.ok();
    }

    /**
     * 禁用用户时，移除auth-service的redis缓存
     * @param id 用户id
     * @param role 角色
     * @return
     */
    @PreAuthorize("hasAuthority('a')")
    @PutMapping("/disable")
    public R logout(@RequestParam Integer id, @RequestParam String role){
        accountService.logout(id, role);
        return R.ok();
    }

    /**
     * 访客注册
     * @return
     */
    @PostMapping("/register")
    public R register(@RequestBody Visitor visitor){
        if (accountService.registerVisitor(visitor)==1) {
            return R.ok();
        }
        return R.err("手机号已注册");
    }

    /**
     * 添加咨询师，只有admin有资格
     * @return
     */
    @PreAuthorize("hasAuthority('a')")
    @PostMapping("/add-counselor")
    public R addCounselor(@RequestBody Counselor counselor){
        if (accountService.addCounselor(counselor)==1) {
            return R.ok();
        }
        return R.err("咨询师用户名已存在");
    }

    /**
     * 添加督导，只有admin有资格
     * @return
     */
    @PreAuthorize("hasAuthority('a')")
    @PostMapping("/add-supervisor")
    public R addSupervisor(@RequestBody Supervisor supervisor){
        if (accountService.addSupervisor(supervisor)==1) {
            return R.ok();
        }
        return R.err("督导用户名已存在");
    }
}
