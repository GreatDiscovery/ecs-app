package com.gavin.app.proxy;

import org.junit.Test;

import java.util.Arrays;

/**
 * bytes数组处理工具
 *
 * @author gavin
 * @date 2020/11/8 9:30 下午
 */
public class ByteUtils {

    @Test
    public void testBytes2Int() {
        int i = 1024;
        byte[] bytes = int2Bytes(i, 3);
        System.out.println(Arrays.toString(bytes));
        int i1 = bytes2Int(bytes, 0, 3);
        System.out.println(i1);
    }


    public static int bytes2Int(byte[] b, int start, int len) {
        int sum = 0;
        int end = start + len;
        for (int i = start; i < end; i++) {
            int n = ((int) b[i]) & 0xff;
            n <<= (--len) * 8;
            sum += n;
        }
        return sum;
    }

    public static byte[] int2Bytes(int value, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[len - i - 1] = (byte) ((value >> 8 * i) & 0xff);
        }
        return b;
    }
}
