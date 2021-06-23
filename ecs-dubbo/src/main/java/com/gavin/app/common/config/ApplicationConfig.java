package com.gavin.app.common.config;

/**
 * @author gavin
 * @date 2021/6/13 下午3:30
 */
public class ApplicationConfig extends AbstractConfig {
    /**
     * 应用名称
     */
    private String name;

    public ApplicationConfig(String name) {
        setName(name);
    }

    public void setName(String name) {
        this.name = name;
        this.id = name;
    }
}
