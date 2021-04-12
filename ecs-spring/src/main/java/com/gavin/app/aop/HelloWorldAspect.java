package com.gavin.app.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author: Gavin
 * @date: 2021/4/12 21:38
 * @description:
 */
@Component    //声明这是一个组件
@Aspect       ///声明这是一个切面Bean
public class HelloWorldAspect {

    //定义切点
    @Pointcut("execution(* com.gavin.app.*..*(..))")
    public void sayings(){}

    /**
     * 前置通知(注解中的sayings()方法，其实就是上面定义pointcut切点注解所修饰的方法名，那只是个代理对象,不需要写具体方法，
     * 相当于xml声明切面的id名，如下，相当于id="embark",用于供其他通知类型引用)
     * <aop:config>
     <aop:aspect ref="mistrel">
     <!-- 定义切点 -->
     <aop:pointcut expression="execution(* *.saying(..))" id="embark"/>
     <!-- 声明前置通知 (在切点方法被执行前调用) -->
     <aop:before method="beforSay" pointcut-ref="embark"/>
     <!-- 声明后置通知 (在切点方法被执行后调用) -->
     <aop:after method="afterSay" pointcut-ref="embark"/>
     </aop:aspect>
     </aop:config>
     */
    @Before("sayings()")
    public void sayHello(){
        System.out.println("注解类型前置通知");
    }

    //后置通知
    @After("sayings()")
    public void sayGoodbey(){
        System.out.println("注解类型后置通知");
    }
    //环绕通知。注意要有ProceedingJoinPoint参数传入。
    @Around("sayings()")
    public void sayAround(ProceedingJoinPoint pjp) throws Throwable{
        System.out.println("注解类型环绕通知..环绕前");
        pjp.proceed();//执行方法
        System.out.println("注解类型环绕通知..环绕后");
    }
}
