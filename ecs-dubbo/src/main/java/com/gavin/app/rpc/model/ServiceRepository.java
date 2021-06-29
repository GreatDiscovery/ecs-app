package com.gavin.app.rpc.model;

import com.gavin.app.common.config.ServiceConfigBase;

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

    public ServiceDescriptor registerService(String pathKey, Class<?> interfaceClass) {
        ServiceDescriptor serviceDescriptor = registerService(interfaceClass);
        if (!interfaceClass.getName().equals(pathKey)) {
            services.putIfAbsent(pathKey, serviceDescriptor);
        }
        return serviceDescriptor;
    }

    public void registerProvider(String serviceKey, Object serviceInstance, ServiceDescriptor serviceModel, ServiceConfigBase<?> serviceConfigBase) {
        ProviderModel providerModel = new ProviderModel(serviceKey, serviceInstance, serviceModel, serviceConfigBase);
        providers.putIfAbsent(serviceKey, providerModel);
    }
}
