package com.eewoo.platform.controller;

import com.eewoo.common.util.R;
import com.eewoo.platform.pojo.vo.response.BindCounselorResponse;
import com.eewoo.platform.pojo.vo.response.VisitorResponse;

import com.eewoo.platform.service.SupervisorService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PreAuthorize("hasAuthority('s')")
@RestController
@RequestMapping("/supervisor")
public class SupervisorController {

    @Autowired
    SupervisorService superservice;
    @GetMapping("/visitor-list")
    public R getvisitorList() {

        List<VisitorResponse> visitor_list = superservice.getVisitorList();
        if(visitor_list.size() > 0)
            return R.ok(visitor_list);
        else
            return R.err("500","未找到任何访客记录");

    }

    @GetMapping("/counsult-record/table")
    public R bindconuselors(@Param("page") Integer page, @Param("size") Integer size)
    {
        List<BindCounselorResponse> list = superservice.bindCounselorsList(page, size);
        if(list.size()>0)
            return R.ok(list);

        else
            return R.err("500","找不到已绑定的督导咨询列表");

    }
}
