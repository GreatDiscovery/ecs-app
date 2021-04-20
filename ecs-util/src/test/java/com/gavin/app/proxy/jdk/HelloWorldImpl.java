package com.gavin.app.proxy.jdk;

import com.gavin.app.proxy.jdk.HelloWorld;

/**
 * @author: Gavin
 * @date: 2021/4/20 9:12
 * @description:
 */
public class HelloWorldImpl implements HelloWorld {
    @Override
    public void sayHello() {
        System.out.println("hello world");
    }
}
