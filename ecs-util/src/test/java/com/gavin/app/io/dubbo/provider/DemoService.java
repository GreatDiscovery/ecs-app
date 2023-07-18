package com.gavin.app.io.dubbo.provider;

import java.util.concurrent.CompletableFuture;

/**
 * @author: Gavin
 * @date: 2020/11/12 17:50
 * @description:
 */
public interface DemoService {
    String sayHello(String name);
}
