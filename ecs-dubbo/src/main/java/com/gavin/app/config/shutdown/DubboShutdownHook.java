package com.gavin.app.config.shutdown;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author gavin
 * @date 2021/6/12 下午10:44
 */
public class DubboShutdownHook extends Thread {

    public static final DubboShutdownHook DUBBO_SHUTDOWN_HOOK = new DubboShutdownHook();

    // 确保整个流程只执行一次，虽然最外部有锁，但是该流程仍然可能会执行多次，因此需要一个标志位
    private final AtomicBoolean registered = new AtomicBoolean(false);

    private final ShutdownHookCallbacks callbacks = ShutdownHookCallbacks.INSTANCE;

    public static DubboShutdownHook getDubboShutdownHook() {
        return DUBBO_SHUTDOWN_HOOK;
    }

    @Override
    public void run() {

    }

    public void callback() {
        callbacks.callback();
    }

    public void register() {
        if (registered.compareAndSet(false, true)) {
            Runtime.getRuntime().addShutdownHook(getDubboShutdownHook());
        }
    }
}
