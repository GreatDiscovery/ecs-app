package com.gavin.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gavin
 * @date 2021/4/16 下午11:13
 */
@SpringBootApplication
@MapperScan("org.gavin.app.dao")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
