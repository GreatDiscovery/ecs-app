package com.gavin.app.spi;

/**
 * @author: Gavin
 * @date: 2020/3/30 13:07
 * @description:
 */
public class PythonDeveloper implements Developer {
    @Override
    public void sayHello() {
        System.out.println("Hello! I am Python developer");
    }
}
