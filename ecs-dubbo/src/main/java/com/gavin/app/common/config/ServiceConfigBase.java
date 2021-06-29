package com.gavin.app.common.config;

import com.gavin.app.common.CommonConstants;
import com.gavin.app.common.URL;
import com.gavin.app.common.util.StringUtils;
import com.gavin.app.rpc.model.ApplicationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gavin
 * @date 2021/6/13 下午3:21
 */
public abstract class ServiceConfigBase<T> extends AbstractServiceConfig {
    // 导出服务的名称
    protected String interfaceName;

    protected Class<?> interfaceClass;

    // 接口的实现类
    protected T ref;

    /**
     * The service name
     */
    protected String path;

    /**
     * The provider configuration
     */
    protected ProviderConfig provider;

    public abstract void export();

    public void setInterface(Class<?> interfaceClass) {
        if (interfaceClass == null || !interfaceClass.isInterface()) {
            throw new IllegalStateException("The interface class " + interfaceClass + " is not a interface!");
        }
        setInterfaceClass(interfaceClass);
        setInterfaceName(interfaceClass.getName());
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public void setRef(T ref) {
        this.ref = ref;
    }

    public Class<?> getInterfaceClass() {
        if (interfaceClass != null) {
            return interfaceClass;
        }
        if (!StringUtils.isEmpty(interfaceName)) {
            try {
                interfaceClass = Class.forName(interfaceName, true, Thread.currentThread().getContextClassLoader());
            } catch (Throwable e) {
                throw new IllegalStateException(e.getMessage());
            }
        }
        return interfaceClass;
    }

    public String getUniqueServiceName() {
        return URL.buildKey(interfaceName, getGroup(), getVersion());
    }

    public void checkDefault() {
        if (provider == null) {
            provider = ApplicationModel.getConfigManager().getDefaultProvider().orElse(new ProviderConfig());
        }
    }

    public void checkProtocol() {
        if (protocols == null) {
            List<ProtocolConfig> list = new ArrayList<>();
            ProtocolConfig protocolConfig = new ProtocolConfig();
            protocolConfig.setName(CommonConstants.DUBBO_PROTOCOL);
            list.add(protocolConfig);
            setProtocols(list);
            ApplicationModel.getConfigManager().addConfig(protocolConfig);
        }
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public T getRef() {
        return ref;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ProviderConfig getProvider() {
        return provider;
    }

    public void setProvider(ProviderConfig provider) {
        this.provider = provider;
    }
}
