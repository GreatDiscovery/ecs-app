package com.gavin.app.util;

import java.util.HashMap;

/**
 * Java12新的计算一个数的最小2的幂次方的数
 *
 * @author gavin
 * @date 2020/1/17 11:00 下午
 */
public class TableSizeForTest {
    public static void main(String[] args) {
        Integer i = 10;
        System.out.println("整数前面0的个数为：" + Integer.numberOfLeadingZeros(i));
        int n = -1 >>> Integer.numberOfLeadingZeros(i - 1);
        System.out.println("最小的2的幂次方为：" + (i = (n < 0) ? 1  : n + 1));
        System.out.println(-1 >>> 28);
        System.out.println(Integer.toHexString(-1));
    }
}
