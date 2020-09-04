package com.gavin.app.util.design.visitor;

/**
 * @author: Gavin
 * @date: 2020/9/4 9:02
 * @description:
 */
public interface Visitor {
    void visit(Engine engine);
    void visit(Body body);
    void visit(Car car);
}
