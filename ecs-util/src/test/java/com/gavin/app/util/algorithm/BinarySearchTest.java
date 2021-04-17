package com.gavin.app.util.algorithm;

import java.util.Arrays;

/**
 * 二分查找测试
 *
 * @author gavin
 * @date 2021/4/17 下午9:12
 */
public class BinarySearchTest {

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        System.out.println("binarySearch(arr, 2) = " + binarySearch(arr, 2));
    }

    public static int binarySearch(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        Arrays.sort(arr);
        int low = 0, high = arr.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] == k) {
                return mid;
            } else if (arr[mid] > k) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return -1;
    }
}
