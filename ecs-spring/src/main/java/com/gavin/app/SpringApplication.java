package com.gavin.app;

import com.gavin.app.config.AppConfig;
import com.gavin.app.service.AService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 拆除springboot部分代码的spring源码
 * 通过解决循环依赖来熟悉spring源码
 * @author gavin
 * @date 2020/5/3 9:58 下午
 */
public class SpringApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        AService aService = ac.getBean(AService.class);
        System.out.println("aService = " + aService);
        ac.destroy();
    }
}
