package com.gavin.app;

import com.gavin.app.common.config.ApplicationConfig;
import com.gavin.app.common.config.RegistryConfig;
import com.gavin.app.common.config.ServiceConfig;
import com.gavin.app.config.bootstrap.DubboBootstrap;
import com.gavin.app.service.DemoService;
import com.gavin.app.service.impl.DemoServiceImpl;

/**
 * @author gavin
 * @date 2021/6/13 下午3:19
 */
public class ProviderApplication {
    public static void main(String[] args) {
        startWithBootstrap();
    }

    public static void startWithBootstrap() {
        ServiceConfig<DemoServiceImpl> serviceConfig = new ServiceConfig<>();
        serviceConfig.setInterface(DemoService.class);
        serviceConfig.setRef(new DemoServiceImpl());

        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap.application(new ApplicationConfig("demo-dubbo-provider"))
                .register(new RegistryConfig("zookeeper://127.0.0.1:2181"))
                .service(serviceConfig)
                .start()
                .await();
    }
}
