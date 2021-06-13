package com.gavin.app.config.context;

import com.gavin.app.common.config.AbstractConfig;
import com.gavin.app.common.config.ApplicationConfig;
import com.gavin.app.common.util.StringUtils;

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

    public void addConfig(ApplicationConfig config) {
        addConfig(config, true);
    }

    public void addConfig(ApplicationConfig config, boolean unique) {
        write(() -> {
            Map<String, AbstractConfig> configMap = configsCache.computeIfAbsent(getTagName(config.getClass()), type -> new HashMap<>());
        });
    }

    public void setApplication(ApplicationConfig config) {
        addConfig(config);
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
