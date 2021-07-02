package com.gavin.app.common.util;

import java.util.Collection;

/**
 * @author: Gavin
 * @date: 2021/7/1 14:16
 * @description:
 */
public class ArrayUtils {

    public static boolean isEmpty(Object[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isNotEmpty(Object[] arr) {
        return !isEmpty(arr);
    }

    public static boolean isEmpty(Collection<String> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<String> collection) {
        return !isEmpty(collection);
    }
}
