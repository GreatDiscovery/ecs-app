package com.gavin.app.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author: Gavin
 * @date: 2021/4/20 9:13
 * @description:
 */
public class MyInvocationHandler implements InvocationHandler {
    private Object target;

    public MyInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("method :"+ method.getName()+" is invoked!");
        return method.invoke(target,args); // 执行相应的目标方法
    }
}
