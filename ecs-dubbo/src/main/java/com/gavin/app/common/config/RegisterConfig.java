package com.gavin.app.common.config;

import com.gavin.app.common.util.StringUtils;

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

    public RegisterConfig(String address) {
        setAddress(address);
    }

    public void setAddress(String address) {
        if (StringUtils.isEmpty(address)) {
            throw new IllegalArgumentException("address can't be null!");
        }
        this.address = address;
    }
}
