package com.gavin.app.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author gavin
 * @date 2020/5/3 10:14 下午
 */
public class CService {
    public CService() {
        System.out.println("Construct CService");
    }

    public void init() {
        System.out.println("Init CService");
    }

    public void destroy() {
        System.out.println("Destroy CService");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("PostConstruct CService");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("PreDestroy CService");
    }
}
