package com.gavin.app.json.attach;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

/**
 * agent包，会被jvm调用到agentmain方法。需要被打成jar包
 * @author gavin
 * @date 2021/10/20 下午11:56
 */
public class Agent {
    private static Logger LOGGER = LoggerFactory.getLogger(Agent.class);

    /**
     * 被jvm调用
     * @param agentArgs
     * @param inst
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        LOGGER.info("[Agent] In agentmain method");
        String className = "com.gavin.app.json.attach.DemoApplication";
        String method = "testExceptionTruncate";
        transformClass(className, inst, method);
    }

    private static void transformClass(String className, Instrumentation instrumentation, String method) {
        Class<?> targetCls = null;
        ClassLoader targetClassLoader = null;
        // see if we can get the class using forName
        try {
            targetCls = Class.forName(className);
            targetClassLoader = targetCls.getClassLoader();
            transform(targetCls, targetClassLoader, instrumentation, method);
            return;
        } catch (Exception ex) {
            LOGGER.error("Class [{}] not found with Class.forName", className);
        }
        // otherwise iterate all loaded classes and find what we want
        for (Class<?> clazz : instrumentation.getAllLoadedClasses()) {
            if (clazz.getName().equals(className)) {
                targetCls = clazz;
                targetClassLoader = targetCls.getClassLoader();
                transform(targetCls, targetClassLoader, instrumentation, method);
                return;
            }
        }
        throw new RuntimeException("Failed to find class [" + className + "]");
    }

    private static void transform(Class<?> clazz, ClassLoader classLoader, Instrumentation instrumentation, String method) {
        TimeWatcherTransformer dt = new TimeWatcherTransformer(clazz.getName(), classLoader, method);
        instrumentation.addTransformer(dt, true);
        try {
            instrumentation.retransformClasses(clazz);
        } catch (Exception ex) {
            throw new RuntimeException("Transform failed for class: [" + clazz.getName() + "]", ex);
        }
    }
}
