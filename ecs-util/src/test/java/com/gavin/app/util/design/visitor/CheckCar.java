package com.gavin.app.util.design.visitor;

/**
 * @author: Gavin
 * @date: 2020/9/4 9:04
 * @description:
 */
public class CheckCar implements Visitor {
    public void visit(Engine engine) {
        System.out.println("Check engine");
    }
    public void visit(Body body) {
        System.out.println("Check body");
    }
    public void visit(Car car) {
        System.out.println("Check car");
    }
}