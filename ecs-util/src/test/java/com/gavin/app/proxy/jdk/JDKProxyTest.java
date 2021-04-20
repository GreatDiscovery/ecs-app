package com.gavin.app.proxy.jdk;

import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.FileOutputStream;
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
    public static void main(String[] args) throws Exception {
        HelloWorld helloWorld = new HelloWorldImpl();
        ClassLoader classLoader = helloWorld.getClass().getClassLoader();
        Class<?>[] interfaces = helloWorld.getClass().getInterfaces();
        HelloWorld proxy = (HelloWorld) Proxy.newProxyInstance(classLoader,
                interfaces,
                new MyInvocationHandler(helloWorld));
        proxy.sayHello();
        byte[] bts = ProxyGenerator.generateProxyClass("com.sun.proxy.$Proxy0", interfaces);
        FileOutputStream fos = new FileOutputStream(new File("./$Proxy.class"));
        fos.write(bts);
        fos.flush();
        fos.close();
    }
}
