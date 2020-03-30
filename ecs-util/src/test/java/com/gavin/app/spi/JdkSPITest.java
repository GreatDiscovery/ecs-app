package com.gavin.app.spi;

import java.util.ServiceLoader;

/**
 * @author: Gavin
 * @date: 2020/3/30 13:09
 * @description:
 */
public class JdkSPITest {
    public static void main(String[] args) {
        ServiceLoader<Developer> serviceLoader = ServiceLoader.load(Developer.class);
        serviceLoader.forEach(Developer::sayHello);
    }
}
