package com.eewoo.platform.controller;

import com.eewoo.common.util.R;
import com.eewoo.platform.pojo.vo.response.VisitorResponse;
import com.eewoo.platform.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasAuthority('v')")
@RestController
@RequestMapping("/visitor")
public class VisitorController {
    @Autowired
    VisitorService visitorService;

    @GetMapping("/info")
    public R getInfo(){
        VisitorResponse info = visitorService.getInfo();
        if (info==null)
            return R.err("无此访客");
        return R.ok(info);
    }
}
