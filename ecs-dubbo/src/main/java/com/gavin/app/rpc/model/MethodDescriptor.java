package com.gavin.app.rpc.model;

import com.gavin.app.common.util.ReflectUtils;

import java.lang.reflect.Method;

/**
 * @author: Gavin
 * @date: 2021/6/17 8:51
 * @description:
 */
public class MethodDescriptor {
    private final Method method;

    private final String methodName;

    private final Class<?>[] parameterClasses;

    private final Class<?> returnClasses;

    /**
     * get class array desc.
     * [int.class, boolean[].class, Object.class] => "I[ZLjava/lang/Object;"
     */
    private final String paramDesc;

    public MethodDescriptor(Method method) {
        this.method = method;
        this.methodName = method.getName();
        this.parameterClasses = method.getParameterTypes();
        this.returnClasses = method.getReturnType();
        this.paramDesc = ReflectUtils.getDesc(parameterClasses);
    }

    public Method getMethod() {
        return method;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameterClasses() {
        return parameterClasses;
    }

    public Class<?> getReturnClasses() {
        return returnClasses;
    }

    public String getParamDesc() {
        return paramDesc;
    }
}
