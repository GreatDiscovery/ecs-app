package com.gavin.app.rpc.model;

import com.gavin.app.common.config.ServiceConfigBase;

/**
 * @author: Gavin
 * @date: 2021/6/16 16:35
 * @description:
 */
public class ProviderModel {
    /**
     * 服务名称
     */
    private String serviceKey;
    /**
     * 实现类实例
     */
    private Object serviceInstance;

    /**
     * 服务的描述信息
     */
    private ServiceDescriptor serviceModel;
    /**
     * 服务的配置信息
     */
    private ServiceConfigBase<?> serviceConfigBase;

    public ProviderModel(String serviceKey, Object serviceInstance, ServiceDescriptor serviceModel, ServiceConfigBase<?> serviceConfigBase) {
        this.serviceKey = serviceKey;
        this.serviceInstance = serviceInstance;
        this.serviceModel = serviceModel;
        this.serviceConfigBase = serviceConfigBase;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    public Object getServiceInstance() {
        return serviceInstance;
    }

    public void setServiceInstance(Object serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    public ServiceDescriptor getServiceModel() {
        return serviceModel;
    }

    public void setServiceModel(ServiceDescriptor serviceModel) {
        this.serviceModel = serviceModel;
    }

    public ServiceConfigBase<?> getServiceConfigBase() {
        return serviceConfigBase;
    }

    public void setServiceConfigBase(ServiceConfigBase<?> serviceConfigBase) {
        this.serviceConfigBase = serviceConfigBase;
    }
}
