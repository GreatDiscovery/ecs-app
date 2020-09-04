package com.gavin.app.util.design.visitor;

/**
 * @author: Gavin
 * @date: 2020/9/4 9:03
 * @description:
 */
public class PrintCar implements Visitor {
    public void visit(Engine engine) {
        System.out.println("Visiting engine");
    }
    public void visit(Body body) {
        System.out.println("Visiting body");
    }
    public void visit(Car car) {
        System.out.println("Visiting car");
    }
}
