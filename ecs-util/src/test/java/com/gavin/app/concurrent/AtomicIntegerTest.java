package com.gavin.app.concurrent;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

/**
 * AtomicInteger边界值控制
 * @author gavin
 * @date 2021/3/5 下午10:35
 */
public class AtomicIntegerTest {
    @Test
    public void test() {
        AtomicInteger atomic = new AtomicInteger(Integer.MAX_VALUE);
        System.out.println(atomic.get());
        atomic.getAndUpdate(operand -> {
            if (operand >= Integer.MAX_VALUE) {
                return 0;
            }
            return operand + 1;
        });
        System.out.println(atomic.get());
    }
}
