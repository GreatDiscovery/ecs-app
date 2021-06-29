package com.gavin.app.common.config;

import com.gavin.app.common.util.StringUtils;

/**
 * @author: Gavin
 * @date: 2021/6/18 17:51
 * @description:
 */
public class ProtocolConfig extends AbstractConfig {

    public ProtocolConfig() {
    }

    /**
     * Protocol name
     */
    private String name;

    public ProtocolConfig(String name) {
        setName(name);
    }

    public void setName(String name) {
        this.name = name;
        if (!StringUtils.isEmpty(name) && StringUtils.isEmpty(id)) {
            this.id = name;
        }
    }
}
