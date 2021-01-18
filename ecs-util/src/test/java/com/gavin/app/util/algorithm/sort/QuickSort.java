package com.gavin.app.util.algorithm.sort;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: Gavin
 * @date: 2019/12/30 14:46
 * @description:
 */
public class QuickSort {
    public static void quickSort(int[] arr, int start, int end) {
        if (start <= end) {
            int i = start;
            int j = end;
            int target = arr[i];
            while (i < j) {
                while (i < j &&  arr[j] >= target) {
                    j--;
                }

                while (i < j && arr[i] <= target) {
                    i++;
                }

                int tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
            }
            arr[start] = arr[i];
            arr[i] = target;
            quickSort(arr, start, i - 1);
            quickSort(arr, i + 1, end);
        }
    }

    public static void main(String[] args) {
        int[] arr = {4, 7, 1, 2, 8, 13, 5, 6, 3, 2, 4, 4};
        quickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }
}

