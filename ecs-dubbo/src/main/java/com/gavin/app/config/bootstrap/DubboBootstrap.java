package com.gavin.app.config.bootstrap;

import com.gavin.app.common.config.ApplicationConfig;
import com.gavin.app.common.config.Environment;
import com.gavin.app.common.config.RegisterConfig;
import com.gavin.app.common.config.ServiceConfigBase;
import com.gavin.app.config.shutdown.DubboShutdownHook;
import com.gavin.app.config.context.ConfigManager;
import com.gavin.app.config.shutdown.ShutdownHookCallbacks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        configManager = new ConfigManager();
        environment = new Environment();
        DubboShutdownHook.getDubboShutdownHook().register();
        ShutdownHookCallbacks.INSTANCE.addCallback(DubboBootstrap.this::destroy);
    }

    private void destroy() {
        // todo 资源释放
    }

    public DubboBootstrap application(ApplicationConfig application) {
        configManager.setApplication(application);
        return this;
    }

    public DubboBootstrap register(RegisterConfig registerConfig) {
        configManager.addRegister(registerConfig);
        return this;
    }

    public DubboBootstrap service(ServiceConfigBase<?> serviceConfigBase) {
        configManager.addService(serviceConfigBase);
        return this;
    }

    public DubboBootstrap start() {
        return this;
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
