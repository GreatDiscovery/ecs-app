package com.gavin.app.util.algorithm.sort;

import java.util.Arrays;

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

//    public static void quickSort(int[] arr, int left, int right) {
//
//        //数组最左边小于最右边不合法,直接退出
//        if (left > right) {
//            return;
//        }
//
//        //定义变量i指向最左边
//        int i = left;
//
//        //定义变量j指向最右边
//        int j = right;
//
//        //定义左边为基准元素base
//        int base = arr[left];
//
//        //只要i和j没有指向同一个元素,就继续交换
//        while (i != j) {
//
//            //从右向左寻找第一个小于基准元素的数
//            while (arr[j] >= base && i < j) {
//                j--;
//            }
//
//            //从左向右寻找第一个大于基准元素的数
//            while (arr[i] <= base && i < j) {
//                i++;
//            }
//
//            //执行到这里证明找到了i和j指向的元素
//            //交换i和j指向的元素
//            int temp = arr[i];
//            arr[i] = arr[j];
//            arr[j] = temp;
//        }
//
//        //将i和j共同指向的元素与基准元素交换
//        arr[left] = arr[i];
//        arr[i] = base;
//
//        //对左边进行快速排序
//        quickSort(arr, left, i - 1);
//
//        //对右边进行快速排序
//        quickSort(arr, i + 1, right);
//    }

    public static void main(String[] args) {
        int[] arr = {4, 7, 1, 2, 8, 13, 5, 6, 3, 2, 4, 4};
        quickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }
}
