package com.gavin.app.jmx;

/**
 * @author gavin
 * @date 2020/3/7 10:10 下午
 */
public interface HelloMBean
{
    public String getName();

    public void setName(String name);

    public String getAge();

    public void setAge(String age);

    public void helloWorld();

    public void helloWorld(String str);

    public void getTelephone();
}