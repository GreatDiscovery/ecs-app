package com.gavin.app.config.shutdown;

import com.gavin.app.common.function.ThrowableAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * 真正的回调方法
 * @author gavin
 * @date 2021/6/12 下午10:55
 */
public class ShutdownHookCallbacks {
    public static final ShutdownHookCallbacks INSTANCE = new ShutdownHookCallbacks();

    private final List<ShutdownHookCallback> callbacks = new ArrayList<>();

    public ShutdownHookCallbacks addCallback(ShutdownHookCallback shutdownHookCallback) {
        synchronized (ShutdownHookCallback.class) {
            this.callbacks.add(shutdownHookCallback);
        }
        return this;
    }

    public Collection<ShutdownHookCallback> getCallbacks() {
        synchronized (this) {
            Collections.sort(callbacks);
        }
        return callbacks;
    }

    public void callback() {
        // 这里的目的是确保lamdba表达式中及时抛出异常
        getCallbacks().forEach(callback -> ThrowableAction.execute(this::callback));
    }

}
