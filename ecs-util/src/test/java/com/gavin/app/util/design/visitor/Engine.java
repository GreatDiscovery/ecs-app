package com.gavin.app.util.design.visitor;

/**
 * @author: Gavin
 * @date: 2020/9/4 9:05
 * @description:
 */
public class Engine implements Visitable {
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}