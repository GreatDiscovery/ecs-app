package com.gavin.app.util.design.visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Gavin
 * @date: 2020/9/4 9:06
 * @description:
 */
public class Car {
    private List<Visitable> visit = new ArrayList<>();
    public void addVisit(Visitable visitable) {
        visit.add(visitable);
    }
    public void show(Visitor visitor) {
        for (Visitable visitable: visit) {
            visitable.accept(visitor);
        }
    }
}