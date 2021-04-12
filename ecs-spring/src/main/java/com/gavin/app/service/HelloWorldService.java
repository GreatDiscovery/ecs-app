package com.gavin.app.service;

import com.gavin.app.service.IHelloWorldService;
import org.springframework.stereotype.Component;

/**
 * @author: Gavin
 * @date: 2021/4/12 21:36
 * @description:
 */

@Component("HelloWorldService")
public class HelloWorldService implements IHelloWorldService {
    @Override
    public void sayHello() {
        System.out.println("你好！Spring AOP——（即这个为主要业务）");
    }
}
