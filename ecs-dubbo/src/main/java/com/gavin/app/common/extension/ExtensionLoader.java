package com.gavin.app.common.extension;

import com.gavin.app.common.config.Environment;
import com.gavin.app.common.util.Holder;
import com.gavin.app.config.context.ConfigManager;
import org.redisson.misc.Hash;

import java.util.HashMap;
import java.util.Map;
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

    /**
     * 缓存所有的class，通过文件加载进来的 ，k/v=名称/类对象
     */
    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();

    /**
     * 缓存类和对象的对应关系
     */
    private Map<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();

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
        Object instance;
        if (holder.get() == null) {
            synchronized (holder) {
                instance = holder.get();
                if (instance == null) {
                    instance = createExtension(name, wrap);
                    holder.set(instance);
                }
            }
        }
        return (T) holder.get();
    }

    public Holder<Object> getOrCreateHolder(String name) {
        Holder<Object> holder = cachedInstances.get(name);
        if (holder == null) {
            cachedInstances.putIfAbsent(name, new Holder<>());
        }
        return cachedInstances.get(name);
    }

    public T createExtension(String name, boolean wrap) {
        Class<?> aClass = getExtensionClasses().get(name);
        if (aClass == null) {
            throw new IllegalStateException("no such name = " + name + " class");
        }
        T instance = (T) EXTENSION_INSTANCES.get(aClass);
        if (instance == null) {
            try {
                EXTENSION_INSTANCES.putIfAbsent(aClass, aClass.newInstance());
                instance = (T) EXTENSION_INSTANCES.get(aClass);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (wrap) {
            // 反射用扩展类包住instance
        }
        return instance;
    }

    private Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> classes = cachedClasses.get();
        if (classes == null) {
            synchronized (cachedClasses) {
                classes = cachedClasses.get();
                if (classes == null) {
                    classes = loadExtensionClasses();
                    cachedClasses.set(classes);
                }
            }
        }
        return cachedClasses.get();
    }

    /**
     * fixme 暂时写死某几个类对象
     * @return
     */
    private Map<String, Class<?>> loadExtensionClasses() {
        Map<String, Class<?>> map = new HashMap<>();
        map.put("config", ConfigManager.class);
        map.put("environment", Environment.class);
        return map;
    }
}
