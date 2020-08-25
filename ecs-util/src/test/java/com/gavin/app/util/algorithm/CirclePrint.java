package com.gavin.app.util.algorithm;

import java.util.ArrayList;

/**
 * 约瑟夫环问题，求最后剩下的人
 *https://leetcode-cn.com/problems/yuan-quan-zhong-zui-hou-sheng-xia-de-shu-zi-lcof/solution/javajie-jue-yue-se-fu-huan-wen-ti-gao-su-ni-wei-sh/
 *
 * @author gavin
 * @date 2020/8/25 4:17 下午
 */
public class CirclePrint {
    public static void main(String[] args) {

    }

    public int lastRemaining(int n, int m) {
        ArrayList<Integer> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        int idx = 0;
        while (n > 1) {
            idx = (idx + m - 1) % n;
            list.remove(idx);
            n--;
        }
        return list.get(0);
    }
}
