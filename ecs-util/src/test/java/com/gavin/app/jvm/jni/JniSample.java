package com.gavin.app.jvm.jni;

/**
 * jni例子
 * 参考https://segmentfault.com/a/1190000022812099
 * gcc -fPIC -I /Library/Java/JavaVirtualMachines/jdk1.8.0_211.jdk/Contents/Home/include/ -shared -o libThreadNative.so thread.c
 * @author gavin
 * @date 2021/11/20 下午4:12
 */
public class JniSample {
    static {
        // 装载库，保证JVM在启动的时候就会装载，这里的库指的是C程序生成的动态链接库
        // Linux下是.so文件，Windows下是.dll文件
        System.loadLibrary("ThreadNative");
    }

    public static void main(String[] args) {
        new MyThread(() -> System.out.println("Java run method...")).start();
    }
}

class MyThread implements Runnable {
    private Runnable target;

    public MyThread(Runnable target) {
        this.target = target;
    }

    @Override
    public void run() {
        if (target != null) {
            target.run();
        }
    }

    public synchronized void start() {
        start0();
    }

    private native void start0();
}