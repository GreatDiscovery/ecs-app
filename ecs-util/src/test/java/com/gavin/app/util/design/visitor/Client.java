package com.gavin.app.util.design.visitor;

/**
 * 访问者模式客户端，用于修改class文件结构
 * https://juejin.im/entry/6844903582056054791
 *
 * @author: Gavin
 * @date: 2020/9/4 9:06
 * @description:
 */
public class Client {
    public static void main(String[] args) {
        Car car = new Car();
        car.addVisit(new Body());
        car.addVisit(new Engine());

        Visitor print = new PrintCar();
        Visitor check = new CheckCar();
        car.show(print);
        car.show(check);
    }
}
