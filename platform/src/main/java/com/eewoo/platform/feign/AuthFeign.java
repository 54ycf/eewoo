package com.eewoo.platform.feign;

import com.eewoo.common.pojo.Counselor;
import com.eewoo.common.security.LoginUser;
import com.eewoo.common.util.R;
import com.eewoo.common.pojo.Supervisor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("auth-service")
public interface AuthFeign {

    @GetMapping("/auth")
    R<LoginUser> parseToken(@RequestParam("token") String token);

    @PutMapping("/account/disable")
    R logout(@RequestParam Integer id, @RequestParam String role, @RequestHeader String token);

    @PostMapping("/account/add-counselor")
    R addCounselor(@RequestBody Counselor counselor, @RequestHeader String token);

    @PostMapping("/account/add-supervisor")
    R addSupervisor(@RequestBody Supervisor supervisor, @RequestHeader String token);
}
