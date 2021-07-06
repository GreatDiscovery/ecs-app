package com.gavin.app.common.util;

import com.gavin.app.common.CommonConstants;

/**
 * @author: Gavin
 * @date: 2021/7/6 10:24
 * @description:
 */
public class NetUtils {

    public static boolean isInvalidLocalHost(String host) {
        return host == null
                || host.length() == 0
                || host.equalsIgnoreCase(CommonConstants.LOCALHOST_KEY)
                || host.equals(CommonConstants.ANYHOST_VALUE)
                || host.startsWith("127.");
    }
}
