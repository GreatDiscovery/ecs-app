package com.gavin.app.jmx;

import java.lang.management.ManagementFactory;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * https://www.cnblogs.com/dongguacai/p/5900507.html
 * 利用JMX来远程管理程序，可以用来监控一些数据
 * @author gavin
 * @date 2020/3/7 10:10 下午
 */
public class HelloAgent
{
    public static void main(String[] args) throws JMException, Exception
    {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName helloName = new ObjectName("jmxBean:name=hello");
        //create mbean and register mbean
        server.registerMBean(new Hello(), helloName);
        Thread.sleep(60*60*1000);
    }
}