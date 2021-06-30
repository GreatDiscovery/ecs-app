package com.gavin.app.common.bytecode;

import java.lang.reflect.InvocationTargetException;
import java.nio.channels.IllegalSelectorException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 代理包装类
 *
 * @author: Gavin
 * @date: 2021/6/30 21:07
 * @description:
 */
public abstract class Wrapper {
    private static Map<Class<?>, Wrapper> WRAPPER_MAP = new ConcurrentHashMap<>();
    private static String[] EMPTY_STRING_ARRAY = new String[0];
    private static String[] OBJECT_METHODS = {"getClass", "toString", "equals", "hashCode"};
    private static Wrapper OBJECT_WRAPER = new Wrapper() {
        @Override
        public String[] getPropertyNames() {
            return EMPTY_STRING_ARRAY;
        }

        @Override
        public Class<?> getPropertyType(String pn) {
            return null;
        }

        @Override
        public boolean hasProperty(String name) {
            return false;
        }

        @Override
        public Object getPropertyValue(Object instance, String pn) throws IllegalArgumentException {
            return null;
        }

        @Override
        public void setPropertyValue(Object instance, String pn, Object pv) throws IllegalArgumentException {
            throw new IllegalStateException("no such property");
        }

        @Override
        String[] getMethodNames() {
            return OBJECT_METHODS;
        }

        @Override
        String[] getDeclaredMethodNames() {
            return OBJECT_METHODS;
        }

        @Override
        Object invokeMethod(Object instance, String mn, Class<?>[] types, Object[] args) throws NoSuchMethodException, InvocationTargetException {
            if ("getClass".equals(mn)) {
                return instance.getClass();
            } else if ("toString".equals(mn)) {
                return instance.toString();
            } else if ("hashCode".equals(mn)) {
                return instance.hashCode();
            } else if ("equals".equals(mn)) {
                if (args.length != 1) {
                    throw new IllegalArgumentException("equals.args.length != 1");
                }
                return instance.equals(args[0]);
            }
            throw new IllegalStateException("no such method!");
        }
    };
    /**
     * get property name array.
     *
     * @return property name array.
     */
    abstract public String[] getPropertyNames();

    /**
     * get property type.
     *
     * @param pn property name.
     * @return Property type or nul.
     */
    abstract public Class<?> getPropertyType(String pn);

    /**
     * has property.
     *
     * @param name property name.
     * @return has or has not.
     */
    abstract public boolean hasProperty(String name);

    /**
     * get property value.
     *
     * @param instance instance.
     * @param pn       property name.
     * @return value.
     */
    abstract public Object getPropertyValue(Object instance, String pn) throws IllegalArgumentException;

    /**
     * set property value.
     *
     * @param instance instance.
     * @param pn       property name.
     * @param pv       property value.
     */
    abstract public void setPropertyValue(Object instance, String pn, Object pv) throws IllegalArgumentException;


    /**
     * get method name array.
     *
     * @return method name array.
     */
    abstract String[] getMethodNames();

    /**
     * get method name array.
     *
     * @return method name array.
     */
    abstract String[] getDeclaredMethodNames();

    /**
     * invoke method.
     *
     * @param instance instance.
     * @param mn       method name.
     * @param types
     * @param args     argument array.
     * @return return value.
     */
    abstract Object invokeMethod(Object instance, String mn, Class<?>[] types, Object[] args) throws NoSuchMethodException, InvocationTargetException;

    /**
     * 获取代理类
     * @param type
     * @return
     */
    public static Wrapper getWrapper(Class<?> type) {
        if (Object.class == type) {
            return OBJECT_WRAPER;
        }
        return WRAPPER_MAP.computeIfAbsent(type, Wrapper::makeWrapper);
    }

    /**
     * 调用java
     * @param type
     * @return
     */
    private static Wrapper makeWrapper(Class<?> type) {
        return null;
    }
}
