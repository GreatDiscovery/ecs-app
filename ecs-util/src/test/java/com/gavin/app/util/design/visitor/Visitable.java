package com.gavin.app.util.design.visitor;

/**
 * @author: Gavin
 * @date: 2020/9/4 9:04
 * @description:
 */
public interface Visitable {
    void accept(Visitor visitor);
}