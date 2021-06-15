package com.gavin.app.common.config;

import com.gavin.app.common.URL;
import com.gavin.app.common.util.PojoUtils;
import com.gavin.app.common.util.StringUtils;

import java.util.Map;

/**
 * 注册中心的配置
 *
 * @author gavin
 * @date 2021/6/13 下午4:55
 */
public class RegisterConfig extends AbstractConfig {

    /**
     * 配置中心URL
     */
    private String address;

    private String host;

    private Integer port;

    private String userName;

    private String password;

    private Map<String, String> parameters;

    public RegisterConfig(String address) {
        setAddress(address);
    }

    public void setAddress(String address) {
        if (StringUtils.isEmpty(address)) {
            throw new IllegalArgumentException("address can't be null!");
        }
        this.address = address;
        URL url = URL.valueOf(address);
        PojoUtils.updatePropertyIfAbsent(this::getUserName, this::setUserName, url.getUserName());
        PojoUtils.updatePropertyIfAbsent(this::getPassword, this::setPassword, url.getPassword());
        PojoUtils.updatePropertyIfAbsent(this::getParameters, this::setParameters, url.getParameters());
    }

    public String getAddress() {
        return address;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
