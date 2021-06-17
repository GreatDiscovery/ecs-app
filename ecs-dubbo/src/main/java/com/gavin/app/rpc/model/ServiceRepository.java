package com.gavin.app.rpc.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务仓库，用来存放生产者、消费者、服务等
 *
 * @author: Gavin
 * @date: 2021/6/16 16:21
 * @description:
 */
public class ServiceRepository {
    // service
    private Map<String, ServiceDescriptor> services = new ConcurrentHashMap<>();

    // providers
    private Map<String, ProviderModel> providers = new ConcurrentHashMap<>();

    // consumers
    private Map<String, ConsumerModel> consumers = new ConcurrentHashMap<>();

    public ServiceDescriptor registerService(Class<?> interfaceClass) {
        return services.computeIfAbsent(interfaceClass.getName(), _k -> new ServiceDescriptor(interfaceClass));
    }
}
