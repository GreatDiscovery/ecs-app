package com.gavin.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author gavin
 * @date 2020/5/3 10:03 下午
 */
@Service
public class AService {
    @Autowired
    private BService bService;

    public AService() {
        System.out.println("Construct AService");
    }

    public void init() {

    }
}
