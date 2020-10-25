package com.gavin.app.util.design.single;

/**
 * 单例设计模式
 *
 * @author gavin
 * @date 2020/10/25 4:54 下午
 */
public class SingleInstance {
    private static volatile SingleInstance singleInstance;

    private SingleInstance() {

    }

    public static SingleInstance getInstance() {
        if (singleInstance == null) {
            synchronized (SingleInstance.class) {
                if (singleInstance == null) {
                    singleInstance = new SingleInstance();
                }
            }
        }
        return singleInstance;
    }

}
