package com.gavin.app.common.util;

import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: Gavin
 * @date: 2021/6/23 17:55
 * @description:
 */
public class MethodUtils {

    public static final Set<Class<?>> SIMPLE_TYPE = new HashSet<Class<?>>(8) {{
        add(String.class);
    }};
    /**
     * 判断是否是get方法
     *
     * @return
     */
    public static boolean isGetter(Method method) {
        String name = method.getName();
        return (name.startsWith("get") || name.startsWith("is")) &&
                !"get".equals(name) && !"is".equals(name) &&
                !"getClass".equals(name) && !"getObject".equals(name) &&
                Modifier.isPublic(method.getModifiers()) &&
                method.getParameterTypes().length == 0 &&
                (ClassUtils.isPrimitiveOrWrapper(method.getReturnType()) || isSimpleType(method.getReturnType()));
    }

    private static boolean isSimpleType(Class<?> clz) {
        return SIMPLE_TYPE.contains(clz);
    }

    /**
     * getName -> name， isSuccess -> success
     * @param
     * @return
     */
    public static String getGetterAttribute(String name) {
        int i = name.startsWith("get") ? 3 : 2;
        return name.substring(i, i + 1).toLowerCase() + name.substring(i + 1);
    }

    public static void main(String[] args) {
        System.out.println(getGetterAttribute("getName"));
    }
}
