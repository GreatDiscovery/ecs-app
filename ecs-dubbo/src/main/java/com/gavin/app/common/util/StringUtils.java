package com.gavin.app.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author gavin
 * @date 2021/6/13 下午4:28
 */
public class StringUtils {
    public static final String EMPTY_STRING = "";
    /**
     * 将驼峰名称变为分隔符名称
     * @param camelName
     * @param split
     * @return
     */
    public static String camelToSplitName(String camelName, String split) {
        if (isEmpty(camelName)) {
            return camelName;
        }
        StringBuilder sb = null;
        for (int i = 0; i < camelName.length(); i++) {
            char ch = camelName.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                if (sb == null) {
                    sb = new StringBuilder();
                    if (i > 0) {
                        sb.append(camelName, 0, i);
                    }
                }
                if (i > 0) {
                    sb.append(split);
                }
                sb.append(Character.toLowerCase(ch));
            } else if (sb != null) {
                sb.append(ch);
            }
        }
        return sb == null ? camelName : sb.toString();
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static String join(String[] arr) {
        if (ArrayUtils.isEmpty(arr)) {
            return EMPTY_STRING;
        }
        StringBuilder buffer = new StringBuilder();
        for (String s : arr) {
            buffer.append(s);
        }
        return buffer.toString();
    }

    public static String join(String[] arr, String split) {
        if (ArrayUtils.isEmpty(arr)) {
            return EMPTY_STRING;
        }
        StringBuilder buffer = new StringBuilder();
        boolean isFirst = true;
        for (String s : arr) {
            if (isFirst) {
                isFirst = false;
            } else {
                buffer.append(split);
            }
            buffer.append(s);
        }
        return buffer.toString();
    }

    public static String join(Collection<String> collection, String split) {
        if (ArrayUtils.isEmpty(collection) || StringUtils.isEmpty(split)) {
            return EMPTY_STRING;
        }
        StringBuilder buffer = new StringBuilder();
        boolean isFirst = true;
        for (String s : collection) {
            if (isFirst) {
                isFirst = false;
            } else {
                buffer.append(split);
            }
            buffer.append(s);
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(String.valueOf(i));
        }
        System.out.println(join(list, ","));
    }
}
