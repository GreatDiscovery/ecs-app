package com.gavin.app.common.config;

import com.gavin.app.config.bootstrap.DubboBootstrap;
import com.gavin.app.rpc.model.ServiceDescriptor;
import com.gavin.app.rpc.model.ServiceRepository;

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
        doExport();
        exported();
    }

    // 真正实现暴露服务的方法
    protected synchronized void doExport() {
        doExportUrls();
    }

    private void doExportUrls() {
        ServiceRepository repository = new ServiceRepository();
        ServiceDescriptor descriptor = new ServiceDescriptor();

    }

    public void exported() {

    }

    public DubboBootstrap getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(DubboBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }
}
