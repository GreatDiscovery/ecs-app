package com.gavin.app.config.context;

import com.gavin.app.common.config.AbstractConfig;
import com.gavin.app.common.config.ApplicationConfig;
import com.gavin.app.common.config.RegisterConfig;
import com.gavin.app.common.config.ServiceConfigBase;
import com.gavin.app.common.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 配置管理
 *
 * @author gavin
 * @date 2021/6/10 下午11:17
 */
public class ConfigManager {

    /**
     * The suffix container
     */
    private static final String[] SUFFIXES = new String[]{"Config", "Bean", "ConfigBase"};

    private Map<String, Map<String, AbstractConfig>> configsCache = new HashMap<>();

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public ConfigManager() {
    }

    public void addConfig(AbstractConfig config) {
        addConfig(config, true);
    }

    public void addConfig(AbstractConfig config, boolean unique) {
        write(() -> {
            Map<String, AbstractConfig> configMap = configsCache.computeIfAbsent(getTagName(config.getClass()), type -> new HashMap<>());
            addIfAbsent(config, configMap, true);
        });
    }

    public <C extends AbstractConfig> Collection<C> getConfigs(String configType) {
        return (Collection<C>) read(() -> getConfigMap(configType).values());
    }

    public <C extends AbstractConfig> Map<String, C> getConfigMap(String configType) {
        return (Map<String, C>) read(() -> configsCache.getOrDefault(configType, Collections.emptyMap()));
    }

    public void setApplication(ApplicationConfig config) {
        addConfig(config);
    }

    public void addRegister(RegisterConfig registerConfig) {
        addConfig(registerConfig);
    }

    public void addService(ServiceConfigBase<?> serviceConfigBase) {
        addConfig(serviceConfigBase);
    }

    public Collection<ServiceConfigBase> getServices() {
        return getConfigs(getTagName(ServiceConfigBase.class));
    }

    private <V> V read(Callable<V> callable) {
        V value = null;
        Lock readLock = readWriteLock.readLock();
        try {
            readLock.lock();
            value = callable.call();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            readLock.unlock();
        }
        return value;
    }

    private void write(Runnable runnable) {
        write(() -> {
            runnable.run();
            return null;
        });
    }

    private <V> V write(Callable<V> callable) {
        V value = null;
        Lock writeLock = readWriteLock.writeLock();
        try {
            writeLock.lock();
            value = callable.call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e.getCause());
        } finally {
            writeLock.unlock();
        }
        return value;
    }

    private String getTagName(Class<?> cls) {
        String simpleName = cls.getSimpleName();
        for (String suffix : SUFFIXES) {
            if (simpleName.endsWith(suffix)) {
                simpleName = simpleName.substring(0, simpleName.length() - suffix.length());
                break;
            }
        }
        return StringUtils.camelToSplitName(simpleName, "-");
    }

    static <C extends AbstractConfig> void addIfAbsent(C config, Map<String, C> map, boolean unique) {
        String key = config.getId();
        map.put(key, config);
    }
}
