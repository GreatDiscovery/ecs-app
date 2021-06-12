package com.gavin.app.config.bootstrap;

import com.gavin.app.common.config.Environment;
import com.gavin.app.config.shutdown.DubboShutdownHook;
import com.gavin.app.config.context.ConfigManager;
import com.gavin.app.config.shutdown.ShutdownHookCallbacks;

/**
 * dubbo初始化
 *
 * @author gavin
 * @date 2021/6/10 下午11:12
 */
public class DubboBootstrap {

    private static volatile DubboBootstrap instance;

    private ConfigManager configManager;

    private Environment environment;

    public DubboBootstrap getInstance() {
        if (instance == null) {
            synchronized (DubboBootstrap.class) {
                if (instance == null) {
                    instance = new DubboBootstrap();
                }
            }
        }
        return instance;
    }

    private DubboBootstrap() {
        configManager = new ConfigManager();
        environment = new Environment();
        DubboShutdownHook.getDubboShutdownHook().register();
        ShutdownHookCallbacks.INSTANCE.addCallback(DubboBootstrap.this::destroy);
    }

    private void destroy() {
        // todo 资源释放
    }
}
