package com.sky.controller;

import com.sky.commons.TestCommon;
import com.sky.service.RedisService;
import com.sky.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by skyjilygao on 2017/7/14.
 */
@Controller
@RequestMapping("/test")
public class testController {

    @Autowired
    private TestService testService;
    @Autowired
    private RedisService redisService;

    public void test(){
        String str=testService.test("friday" );
        TestCommon TestCommon =new TestCommon();
        TestCommon.a(str);
    }
    @RequestMapping("/index")
    public String index(){
        redisService.cacheSet("test","55555");
        return "a";
    }
}
