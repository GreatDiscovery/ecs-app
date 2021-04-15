package com.gavin.app;

import com.gavin.app.config.AppConfig;
import com.gavin.app.service.IHelloWorldService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author: Gavin
 * @date: 2021/4/12 21:43
 * @description:
 */
public class HelloWorldTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        //通过ApplicationContext.getBean(beanName)动态加载数据（类）【获取Spring容器中已初始化的bean】。
        IHelloWorldService helloWorld = (IHelloWorldService) ac.getBean("HelloWorldService");
//        Arrays.stream(ac.getBeanDefinitionNames()).forEach(System.out::println);
        //执行动态加载到的类的方法
        helloWorld.sayHello();
    }
}
