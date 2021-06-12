package com.gavin.app.config.shutdown;

import com.gavin.app.common.lang.Prioritized;

/**
 * @author gavin
 * @date 2021/6/12 下午10:59
 */
public interface ShutdownHookCallback extends Prioritized {

    /**
     * 调用回调方法
     */
    void callback();
}
