package com.gavin.app.common.config;

import com.gavin.app.common.CommonConstants;
import com.gavin.app.common.URL;
import com.gavin.app.common.bytecode.Wrapper;
import com.gavin.app.common.util.ConfigUtils;
import com.gavin.app.common.util.StringUtils;
import com.gavin.app.config.bootstrap.DubboBootstrap;
import com.gavin.app.config.util.ConfigValidationUtils;
import com.gavin.app.rpc.model.ServiceDescriptor;
import com.gavin.app.rpc.model.ServiceRepository;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gavin
 * @date 2021/6/13 下午3:16
 */
public class ServiceConfig<T> extends ServiceConfigBase<T> {

    private DubboBootstrap bootstrap;

    @Override
    public void export() {
        if (bootstrap == null) {
            bootstrap = DubboBootstrap.getInstance();
            bootstrap.initialize();
        }
        checkAndUpdateSubConfigs();

        doExport();
        exported();
    }

    // 真正实现暴露服务的方法
    protected synchronized void doExport() {
        if (StringUtils.isEmpty(path)) {
            path = interfaceName;
        }
        doExportUrls();
    }

    private void doExportUrls() {
        ServiceRepository repository = new ServiceRepository();
        ServiceDescriptor descriptor = repository.registerService(getInterfaceClass());
        repository.registerProvider(getUniqueServiceName(), ref, descriptor, this);

        List<URL> registryURLs = ConfigValidationUtils.loadRegistries(this, true);

        for (ProtocolConfig protocol : protocols) {
            String key = URL.buildKey(path, group, version);
            // 这里根据group和version不同，注册的是不同的服务
            repository.registerService(key, getInterfaceClass());
            doExportUrlsFor1Protocol(protocol, registryURLs);
        }
    }

    private void doExportUrlsFor1Protocol(ProtocolConfig protocolConfig, List<URL> registryURLs) {
        String name = protocolConfig.getName();
        if (StringUtils.isEmpty(name)) {
            name = CommonConstants.DUBBO_PROTOCOL;
        }
        Map<String, String> map = new HashMap<>();
        map.put(CommonConstants.SIDE_KEY, CommonConstants.PRIVIDER_KEY);
        AbstractServiceConfig.appendRuntimeParameters(map);
        AbstractServiceConfig.appendParameters(map, provider);
        AbstractServiceConfig.appendParameters(map, protocolConfig);
        AbstractServiceConfig.appendParameters(map, this);
        String[] methodNames = Wrapper.getWrapper(interfaceClass).getMethodNames();
        if (methodNames.length == 0) {
            map.put(CommonConstants.METHODS_KEY, "*");
        } else {
            map.put(CommonConstants.METHODS_KEY, StringUtils.join(methodNames, ","));
        }

    }

    /**
     * 已经导出服务
     */
    public void exported() {

    }

    public DubboBootstrap getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(DubboBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    private void checkAndUpdateSubConfigs() {
        // 创建providerConfig
        checkDefault();
        // 创建默认的dubbo protocol
        checkProtocol();
        // 检查注册仓库信息
        checkRegistry();
    }

    /**
     * 给provider绑定一个IP，变量有很多来源，需要考虑加载顺序问题。
     * environment variables -> java system properties -> host property in config file ->
         /etc/hosts -> default network address -> first available network address
     * @param protocolConfig
     * @param registryUrls
     * @param map
     */
    private void findConfigedHosts(ProtocolConfig protocolConfig, List<URL> registryUrls, Map<String, String> map) {
        String value = getValueFromConfig(protocolConfig, CommonConstants.DUBBO_IP_TO_BIND);
    }

    private String getValueFromConfig(ProtocolConfig protocolConfig, String key) {
        String protocolPrefix = protocolConfig.getName().toUpperCase() + "_";
        String value = ConfigUtils.getSystemProperty(protocolPrefix + key);
        if (StringUtils.isEmpty(value)) {
            value = ConfigUtils.getSystemProperty(key);
        }
        return value;
    }

    public static void main(String[] args) {
        try {
            System.out.println(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
