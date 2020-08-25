package com.gavin.app.util.algorithm.sort;

import java.util.Arrays;

/**
 * Arrays.sort()里的插入排序，在数组长度小于47时使用
 *
 * @author: Gavin
 * @date: 2020/1/19 16:59
 * @description:
 */
public class InsertSort {

    public static void insertSort(int[] a, int left, int right) {
        // 一次循环中定下1个位置，这里必须用++i
        for (int i = left, j = i; i < right; j = ++i) {
           int ai = a[i + 1];
           while (ai < a[j]) {
               a[j + 1] = a[j];
               if (j-- == left) {
                   break;
               }
           }
           a[j + 1] = ai;
        }
    }

    public static void main(String[] args) {
        int[] arr = {8, 2, 5, 3, 1};
        insertSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }
}
