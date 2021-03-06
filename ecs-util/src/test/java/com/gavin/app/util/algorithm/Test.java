package com.gavin.app.util.algorithm;

public class Test {
    public void quickSort(int[] arr, int start, int end) {
        int pivot = arr[0];
        int i = start, j = end;
        while (i < j) {
            while (i < j && arr[j] > pivot) {
                j--;
            }
            while (i < j && arr[i] < pivot) {
                i++;
            }
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
            arr[i] = pivot;
        }
        quickSort(arr, start, i - 1);
        quickSort(arr, i + 1, end);
    }

}
