package main.com.gavin.app.util.algorithm;

import java.util.Arrays;

/**
 * @author: Gavin
 * @date: 2019/12/30 14:46
 * @description:
 */
public class QuickSort {
    public static void quickSort(int[] arr, int start, int end) {
        if (start < end) {
            int i = start;
            int j = end;
            int target = arr[i];
            while (i < j) {
                while (i < j && target < arr[j]) {
                    j--;
                }

                while (i < j && target > arr[i]) {
                    i++;
                }

                int tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
            }
            arr[i] = target;
            quickSort(arr, start, i - 1);
            quickSort(arr, i + 1, end);
        }
    }

    public static void main(String[] args) {
        int[] arr = {5, 4, 9, 2, 0, 3, 11, 54, 32, 85};
        quickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }
}
