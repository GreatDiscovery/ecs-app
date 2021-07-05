package com.gavin.app.common;

/**
 * @author gavin
 * @date 2021/6/29 上午12:02
 */
public interface CommonConstants {

    String ANYHOST_VALUE = "0.0.0.0";

    String DUBBO_VERSION_KEY = "dubbo";

    /**
     * package version in the manifest
     */
    String RELEASE_KEY = "release";

    String TIMESTAMP_KEY = "timestamp";

    String PID_KEY = "pid";

    String PROTOCOL_KEY = "protocol";

    String DUBBO_PROTOCOL = "dubbo";

    /**
     * provider还是consumer
     */
    String SIDE_KEY = "side";

    String PRIVIDER_KEY = "provider";

    String METHODS_KEY = "methods";

    String METHOD_KEY = "method";

    /**
     * dubbo部署的服务器IP
     */
    String DUBBO_IP_TO_BIND = "DUBBO_IP_TO_BIND";
}
