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
}
