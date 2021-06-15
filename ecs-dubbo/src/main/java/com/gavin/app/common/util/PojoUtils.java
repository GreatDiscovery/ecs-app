package com.gavin.app.common.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author: Gavin
 * @date: 2021/6/15 13:43
 * @description:
 */
public class PojoUtils {
    public static <T> void updatePropertyIfAbsent(Supplier<T> getMethod, Consumer<T> setMethod, T newValue) {
        if (newValue != null && getMethod.get() == null) {
            setMethod.accept(newValue);
        }
    }
}
