package com.sky.service.impl;

import com.sky.service.TestService;

/**
 * Created by fahaimac on 2017/7/14.
 */
public class TestServiceImpl implements TestService {

    public String test(String string) {
        return "hello "+string;
    }
}
