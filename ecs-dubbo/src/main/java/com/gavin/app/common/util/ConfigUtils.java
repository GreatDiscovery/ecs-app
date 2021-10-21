package com.gavin.app.common.util;

/**
 * @author: Gavin
 * @date: 2021/7/5 18:36
 * @description:
 */
public class ConfigUtils {

    /**
     * 环境变量-》系统变量
     * @param key
     * @return
     */
    public static String getSystemProperty(String key) {
        String value = System.getenv(key);
        if (StringUtils.isEmpty(value)) {
            value = System.getProperty(key);
        }
        return value;
    }
}
