package com.eewoo.chat.controller;

import com.eewoo.common.util.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class TestController {


    @PostMapping("/test")
    public R test(){
        System.out.println("okk");
        return R.ok();
    }
}
