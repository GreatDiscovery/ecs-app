package com.gavin.app.proxy;

/**
 * 实现热加载
 *
 * @author gavin
 * @date 2020/11/8 9:17 下午
 */
public class HotSwapClassLoader extends ClassLoader {
    public HotSwapClassLoader(ClassLoader parent) {
        super(parent);
    }

    public Class loadByte(byte[] classByte) {
        return defineClass(null, classByte, 0, classByte.length);
    }
}
