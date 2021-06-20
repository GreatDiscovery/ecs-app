package com.gavin.app.common.util;

/**
 *  对象的容器
 * @author gavin
 * @date 2021/6/20 下午9:51
 */
public class Holder<T> {

    private volatile T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
