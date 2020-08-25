package com.gavin.app.util.algorithm.sort;

import java.util.Arrays;

/**
 * 归并排序
 *
 * @author: Gavin
 * @date: 2020/8/19 8:48
 * @description:
 */
public class MergeSort {
    public static void main(String[] args) {
        int[] arr = {4, 7, 1, 2, 8, 13, 5, 6, 3, 2, 4, 4};
        mergeSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }

    public static void mergeSort(int[] arr, int start, int end) {
        if (start < end) {
            int mid = (start + end) / 2;
            mergeSort(arr, start, mid);
            mergeSort(arr, mid + 1, end);
            mergeSort(arr, start, mid, end);
        }
    }

    public static void mergeSort(int[] arr, int start, int mid, int end) {
        int[] tmp = new int[end - start + 1];
        int i = start, j = mid + 1;
        int k = 0;
        while (i <= mid && j <= end) {
            if (arr[i] < arr[j]) {
                tmp[k++] = arr[i++];
            } else {
                tmp[k++] = arr[j++];
            }
        }

        while (i <= mid) {
            tmp[k++] = arr[i++];
        }
        while (j <= end) {
            tmp[k++] = arr[j++];
        }

        for (int k2 = 0; k2 < tmp.length; k2++) {
            arr[k2 + start] = tmp[k2];
        }
    }
}
