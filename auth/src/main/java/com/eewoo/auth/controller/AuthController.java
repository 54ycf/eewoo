package com.eewoo.auth.controller;

import com.eewoo.common.security.LoginUser;
import com.eewoo.auth.service.AuthService;
import com.eewoo.common.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    AuthService authService;

    /**
     * 请求token，返回LoginUser
     * @param token
     * @return
     */
    @GetMapping()
    public R<LoginUser> parseToken(@RequestParam("token") String token){
        LoginUser user = authService.parse(token);
        user.setAuthorities(null);
        return R.ok(user);
    }
}
