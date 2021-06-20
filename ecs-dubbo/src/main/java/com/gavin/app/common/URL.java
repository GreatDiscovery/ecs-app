package com.gavin.app.common;

import com.gavin.app.common.util.StringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
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

    /**
     * ip:port
     */
    protected String address;

    protected String path;

    /**
     * 参数
     */
    private Map<String, String> parameters;

    public URL(String protocol,
               String username,
               String password,
               String host,
               int port,
               String path,
               Map<String, String> parameters) {
        this.protocol = protocol;
        this.userName = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.address = getAddress(host, port);
        this.path = path;
        if (parameters == null) {
            parameters = new HashMap<>();
        } else {
            parameters = new HashMap<>(parameters);
            // 配置信息防止被更改
            this.parameters = Collections.unmodifiableMap(parameters);
        }
    }

    private static String getAddress(String host, int port) {
        return port < 0 ? host : host + ":" + port;
    }

    /**
     * protocol://username:password@host:port/path?k1=v1&k2=v2
     * case: zookeeper://127.0.0.1:2181?key=value
     * xxx 优化: 这个方法里生成了很多string对象，可以用startIndex和endIndex来减少对象的创建
     * @param url
     * @return
     */
    public static URL valueOf(String url) {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url == null");
        }
        String protocol = null;
        String userName = null;
        String password = null;
        String host = null;
        int port = 0;
        String path = null;
        Map<String, String> parameters = null;
        int i = url.lastIndexOf("?");
        if (i >= 0) {
            String[] parts = url.substring(i + 1).split("&");
            parameters = new HashMap<>();
            for (String part : parts) {
                part = part.trim();
                if (part.length() > 0) {
                    int j = part.indexOf("=");
                    if (j >= 0) {
                        parameters.put(part.substring(0, i), part.substring(i + 1));
                    }
                }
            }
            url = url.substring(0, i);
        }

        // protocol
        i = url.indexOf("://");
        if (i >= 0) {
            if (i == 0) {
                throw new IllegalArgumentException("protocol == null");
            }
            protocol = url.substring(0, i);
            url = url.substring(i + 3);
        }
        // userName & password

        // host & port
        i = url.lastIndexOf(":");
        if (i >= 0) {
            port = Integer.parseInt(url.substring(i + 1));
            url = url.substring(0, i);
        }
        if (url.length() > 0) {
            host = url;
        }
        return new URL(protocol, userName, password, host, port, path, parameters);
    }

    // group/path:version
    public static String buildKey(String path, String group, String version) {
        StringBuilder buf = new StringBuilder();
        if (!StringUtils.isEmpty(group)) {
            buf.append(group).append("/");
        }
        buf.append(path);
        if (!StringUtils.isEmpty(version)) {
            buf.append(":").append(version);
        }
        return buf.toString();
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
