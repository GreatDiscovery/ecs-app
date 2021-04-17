package com.gavin.app.base;

import io.swagger.models.auth.In;

/**
 * int溢出测试
 *
 * @author gavin
 * @date 2021/4/17 下午7:50
 */
public class OverflowTest {
    public static void main(String[] args) {
        // 补码转原码过程就是，补码的补码就是原码
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MIN_VALUE);
        int a = Integer.MAX_VALUE + 1;
        System.out.println("a = " + a);
        int b = Math.addExact(Integer.MAX_VALUE, 1);
        System.out.println("b = " + b);
    }
}
