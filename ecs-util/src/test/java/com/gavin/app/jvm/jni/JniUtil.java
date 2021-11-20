package com.gavin.app.jvm.jni;

/**
 * 测试Java本地库调用
 *
 * @author gavin
 * @date 2021/11/20 下午3:33
 */
public class JniUtil {

    // 定义 native 方法
    public native String sayHelloWorld();

    // 加载 so 动态库文件
    static {
        System.loadLibrary("HelloWord");
    }

}