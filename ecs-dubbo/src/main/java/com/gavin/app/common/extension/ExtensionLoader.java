package com.gavin.app.common.extension;

import com.gavin.app.common.util.Holder;
import com.gavin.app.config.context.ConfigManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * SPI扩展类
 *
 * @author gavin
 * @date 2021/6/20 下午9:42
 */
public class ExtensionLoader<T> {

    private final ConcurrentMap<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();

    public T getExtension(String name) {
        return getExtension(name, true);
    }

    /**
     * 是否需要将对象包装起来
     * @param name
     * @param wrap
     * @return
     */
    public T getExtension(String name, boolean wrap) {
        Holder<Object> holder = getOrCreateHolder(name);
        // todo 如何完成单例类的加载
        return (T) holder.get();
    }

    public Holder<Object> getOrCreateHolder(String name) {
        Holder<Object> holder = cachedInstances.get(name);
        if (holder == null) {
            holder = cachedInstances.putIfAbsent(name, new Holder<>());
        }
        return holder;
    }
}
