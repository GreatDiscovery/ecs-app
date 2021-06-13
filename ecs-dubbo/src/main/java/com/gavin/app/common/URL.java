package com.gavin.app.common;

import java.io.Serializable;
import java.util.Map;

/**
 * @author gavin
 * @date 2021/6/13 下午4:59
 */
public class URL implements Serializable {
    /**
     * 协议
     */
    protected String protocol;

    protected String userName;

    protected String password;

    /**
     * host to registry
     */
    protected String host;

    /**
     * port to registry
     */
    protected int port;

    protected String path;

    private Map<String, String> parameters;

//    public URL(String protocol,
//               String username,
//               String password,
//               String host,
//               int port,
//               String path,
//               Map<String, String> parameters) {
//        this(protocol, username, password, host, port, path, parameters);
//    }

    /**
     * example: zookeeper://127.0.0.1:2181
     * @param url
     * @return
     */
    public static URL valueOf(String url) {
        return null;
    }
}
