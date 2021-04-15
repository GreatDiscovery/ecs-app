package com.gavin.app.base;

import java.util.BitSet;

/**
 * 测试去重逻辑
 *
 * @author: Gavin
 * @date: 2021/3/18 12:37
 * @description:
 */
public class BitSetTest {
    public static void main(String[] args) {
        BitSet bitSet = new BitSet();
        bitSet.set(5);
        System.out.println(bitSet.get(5));
        System.out.println(bitSet.get(16));
    }
}
