package com.gavin.app.config;

import com.gavin.app.service.CService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author gavin
 * @date 2020/5/3 10:02 下午
 */
@Configuration
@ComponentScan({"com.gavin.app.aop", "com.gavin.app.service"})
public class AppConfig {

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public CService getCService() {
        return new CService();
    }
}
