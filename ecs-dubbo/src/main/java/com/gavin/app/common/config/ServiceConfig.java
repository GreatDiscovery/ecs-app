package com.gavin.app.common.config;

import com.gavin.app.common.URL;
import com.gavin.app.common.util.StringUtils;
import com.gavin.app.config.bootstrap.DubboBootstrap;
import com.gavin.app.config.util.ConfigValidationUtils;
import com.gavin.app.rpc.model.ServiceDescriptor;
import com.gavin.app.rpc.model.ServiceRepository;

import java.util.List;

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

    private void doExportUrlsFor1Protocol(ProtocolConfig protocol, List<URL> registryURLs) {

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
}
