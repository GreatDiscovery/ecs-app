package com.gavin.app.common.util;

/**
 * @author: Gavin
 * @date: 2021/7/1 14:11
 * @description:
 */
public class ClassUtils {

    public static ClassLoader getClassLoader() {
        return getClassLoader(ClassUtils.class);
    }

    public static ClassLoader getClassLoader(Class<?> type) {
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Exception e) {

        }
        if (classLoader == null) {
            classLoader = type.getClassLoader();
        }
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        return classLoader;
    }
}
