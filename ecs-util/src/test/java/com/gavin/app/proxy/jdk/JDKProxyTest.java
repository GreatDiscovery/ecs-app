package com.gavin.app.proxy.jdk;

import java.lang.reflect.Proxy;

/**
 * todo https://www.cnblogs.com/webor2006/p/9847915.html
 * 查看字节码
 *
 * @author: Gavin
 * @date: 2021/4/20 9:15
 * @description:
 */
public class JDKProxyTest {
    public static void main(String[] args) {
        HelloWorld helloWorld = (HelloWorld) Proxy.newProxyInstance(JDKProxyTest.class.getClassLoader(),
                new Class<?>[]{HelloWorld.class},
                new MyInvocationHandler(new HelloWorldImpl()));
        helloWorld.sayHello();
    }
}
