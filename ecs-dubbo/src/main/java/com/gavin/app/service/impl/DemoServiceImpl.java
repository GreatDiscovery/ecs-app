package com.gavin.app.service.impl;

import com.gavin.app.service.DemoService;

/**
 * @author gavin
 * @date 2021/6/13 上午11:17
 */
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {
        return name + " hello!";
    }
}
