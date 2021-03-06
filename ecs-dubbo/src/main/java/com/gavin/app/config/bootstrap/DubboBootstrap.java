package com.gavin.app.config.bootstrap;

import com.gavin.app.common.config.*;
import com.gavin.app.config.shutdown.DubboShutdownHook;
import com.gavin.app.config.context.ConfigManager;
import com.gavin.app.config.shutdown.ShutdownHookCallbacks;
import com.gavin.app.rpc.model.ApplicationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

/**
 * dubbo初始化
 *
 * @author gavin
 * @date 2021/6/10 下午11:12
 */
public class DubboBootstrap {

    Logger logger = LoggerFactory.getLogger(DubboBootstrap.class);

    private Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    private final AtomicBoolean awaited = new AtomicBoolean(false);
    private final ExecutorService executorService = newSingleThreadExecutor();

    private static volatile DubboBootstrap instance;

    private ConfigManager configManager;

    private Environment environment;

    private AtomicBoolean started = new AtomicBoolean(false);
    private AtomicBoolean ready = new AtomicBoolean(true);

    private List<ServiceConfigBase<?>> exportedServices = new ArrayList<>();

    public static DubboBootstrap getInstance() {
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
        configManager = ApplicationModel.getConfigManager();
        environment = ApplicationModel.getEnvironment();
        DubboShutdownHook.getDubboShutdownHook().register();
        ShutdownHookCallbacks.INSTANCE.addCallback(DubboBootstrap.this::destroy);
    }


    public void initialize() {
        // todo 初始化逻辑
    }

    public void destroy() {
        // todo 资源释放
    }

    public DubboBootstrap application(ApplicationConfig application) {
        configManager.setApplication(application);
        return this;
    }

    public DubboBootstrap register(RegistryConfig registryConfig) {
        configManager.addRegister(registryConfig);
        return this;
    }

    public DubboBootstrap service(ServiceConfigBase<?> serviceConfigBase) {
        configManager.addService(serviceConfigBase);
        return this;
    }

    public DubboBootstrap start() {
        if (started.compareAndSet(false, true)) {
            ready.set(false);
            initialize();
            exportService();
            ready.set(true);
        }
        return this;
    }



    // 暴露dubbo服务
    private void exportService() {
        configManager.getServices().forEach(sc -> {
            ServiceConfig serviceConfig = (ServiceConfig) sc;
            serviceConfig.setBootstrap(this);
            serviceConfig.export();
            exportedServices.add(serviceConfig);
        });
    }

    public DubboBootstrap await() {
        if (!awaited.get()) {
            if (!executorService.isShutdown()) {
                executeLocked(() -> {
                    while (!awaited.get()) {
                        if (logger.isInfoEnabled()) {
                            logger.info("awaiting ...");
                        }
                        try {
                            condition.await();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                });
            }
        }
        return this;
    }

    private void executeLocked(Runnable runnable) {
        try {
            lock.lock();
            runnable.run();
        } finally {
            lock.unlock();
        }
    }
}
