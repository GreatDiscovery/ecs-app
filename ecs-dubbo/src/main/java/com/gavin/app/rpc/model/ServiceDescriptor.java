package com.gavin.app.rpc.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Gavin
 * @date: 2021/6/16 16:22
 * @description:
 */
public class ServiceDescriptor {

    private Class<?> interfaceClass;

    private String serviceName;

    private Map<String, List<MethodDescriptor>> methods = new HashMap<>();

    private Map<String, Map<String, MethodDescriptor>> descToMethods = new HashMap<>();

    public ServiceDescriptor(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
        this.serviceName = interfaceClass.getName();
        initMethod();
    }

    private void initMethod() {
        final Method[] methodsToExport = interfaceClass.getMethods();
        for (Method method : methodsToExport) {
            method.setAccessible(true);
            List<MethodDescriptor> methodDescriptors = methods.computeIfAbsent(method.getName(), _k -> new ArrayList<>(1));
            methodDescriptors.add(new MethodDescriptor(method));
        }

        methods.forEach((methodName, methodList) -> {
            Map<String, MethodDescriptor> methodDescriptorMap = descToMethods.computeIfAbsent(methodName, _k -> new HashMap<>());
            methodList.forEach(methodDescriptor -> methodDescriptorMap.put(methodDescriptor.getParamDesc(), methodDescriptor));
        });
    }
}
