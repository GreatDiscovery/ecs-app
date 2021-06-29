package com.gavin.app.common;

/**
 * @author: Gavin
 * @date: 2021/6/29 10:52
 * @description:
 */
public class Version {
    public static final String DEFAULT_DUBBO_PROTOCOL_VERSION = "2.0.2";

    // Dubbo implementation version, usually is jar version.
    private static final String VERSION = DEFAULT_DUBBO_PROTOCOL_VERSION;

    public static String getVersion() {
        return VERSION;
    }
}
