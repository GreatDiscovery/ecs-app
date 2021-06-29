package com.gavin.app.common.config;

import java.util.List;

/**
 * @author gavin
 * @date 2021/6/20 下午8:46
 */
public abstract class AbstractServiceConfig extends AbstractInterfaceConfig {

    /**
     * The protocol list the service will export with
     * Also see {@link #protocolIds}, only one of them will work.
     */
    protected List<ProtocolConfig> protocols;

    /**
     * The id list of protocols the service will export with
     * Also see {@link #protocols}, only one of them will work.
     */
    protected String protocolIds;

    public List<ProtocolConfig> getProtocols() {
        return protocols;
    }

    public void setProtocols(List<ProtocolConfig> protocols) {
        this.protocols = protocols;
    }

    public String getProtocolIds() {
        return protocolIds;
    }

    public void setProtocolIds(String protocolIds) {
        this.protocolIds = protocolIds;
    }
}
