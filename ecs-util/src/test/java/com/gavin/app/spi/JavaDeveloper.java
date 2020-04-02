package com.gavin.app.spi;

/**
 * @author: Gavin
 * @date: 2020/3/30 13:06
 * @description:
 */
public class JavaDeveloper implements Developer {
    @Override
    public void sayHello() {
        System.out.println("Hello! I am Java developer");
    }
}
