package com.gavin.app;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author gavin
 * @date 2019-12-17 22:42
 */
@SpringBootApplication
public class Application {
    @Bean
    public ExitCodeGenerator exitCodeGenerator() {
        // 返回的调用状态码，比如0成功，1失败等
        return () -> 42;
    }
    public static void main(String[] args) {
//        System.exit(SpringApplication.exit(SpringApplication.run(Application.class)));
        SpringApplication.run(Application.class);
    }
}
