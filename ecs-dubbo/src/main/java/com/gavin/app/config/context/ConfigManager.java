package com.gavin.app.config.context;

import com.gavin.app.common.config.*;
import com.gavin.app.common.context.FrameworkExt;
import com.gavin.app.common.util.StringUtils;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.util.Optional.ofNullable;

/**
 * 配置管理
 *
 * @author gavin
 * @date 2021/6/10 下午11:17
 */
public class ConfigManager implements FrameworkExt {

    public static final String NAME = "config";

    private Map<String, Map<String, AbstractConfig>> configsCache = new HashMap<>();

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public ConfigManager() {
    }

    public void addConfig(AbstractConfig config) {
        addConfig(config, true);
    }

    public ApplicationConfig getApplicationOrElseThrow() {
        return getApplication().orElseThrow(() -> new IllegalStateException("no applicationConfig"));
    }

    public Optional<ApplicationConfig> getApplication() {
        return ofNullable(getConfig(AbstractConfig.getTagName(ApplicationConfig.class)));
    }

    public void addConfig(AbstractConfig config, boolean unique) {
        write(() -> {
            Map<String, AbstractConfig> configMap = configsCache.computeIfAbsent(AbstractConfig.getTagName(config.getClass()), type -> new HashMap<>());
            addIfAbsent(config, configMap, true);
        });
    }

    public <C extends AbstractConfig> Collection<C> getConfigs(String configType) {
        return (Collection<C>) read(() -> getConfigMap(configType).values());
    }

    public <C extends AbstractConfig> C getConfig(String configType) {
        return read(() -> {
            Map<String, C> configMap = configsCache.getOrDefault(configType, Collections.EMPTY_MAP);
            if (configMap.size() != 1) {
                return null;
            }
            return configMap.values().iterator().next();
        });
    }

    public <C extends AbstractConfig> Map<String, C> getConfigMap(String configType) {
        return (Map<String, C>) read(() -> configsCache.getOrDefault(configType, Collections.emptyMap()));
    }

    public void setApplication(ApplicationConfig config) {
        addConfig(config);
    }

    public void addRegister(RegistryConfig registryConfig) {
        addConfig(registryConfig);
    }

    public void addService(ServiceConfigBase<?> serviceConfigBase) {
        addConfig(serviceConfigBase);
    }

    public Collection<ServiceConfigBase> getServices() {
        return getConfigs(AbstractConfig.getTagName(ServiceConfigBase.class));
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



    static <C extends AbstractConfig> void addIfAbsent(C config, Map<String, C> map, boolean unique) {
        String key = getId(config);
        map.put(key, config);
    }

    static  <C extends AbstractConfig> String getId(C config) {
        String id = config.getId();
        return !StringUtils.isEmpty(id) ? id : config.getClass().getSimpleName();
    }

    public Optional<ProviderConfig> getDefaultProvider() {
        // todo 如何生成provider
        return Optional.empty();
    }
}
