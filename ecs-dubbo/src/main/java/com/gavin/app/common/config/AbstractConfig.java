package com.gavin.app.common.config;

import java.util.List;

/**
 * @author gavin
 * @date 2021/6/13 下午3:33
 */
public class AbstractConfig {
    /**
     * 配置ID
     */
    protected String id;

    /**
     * 分组
     */
    protected String group;
    /**
     * 接口版本
     */
    protected String version;

    /**
     * The protocol list the service will export with
     * Also see {@link #protocolIds}, only one of them will work.
     */
    protected List<ProtocolConfig> protocols;

    public String getId() {
        return id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
