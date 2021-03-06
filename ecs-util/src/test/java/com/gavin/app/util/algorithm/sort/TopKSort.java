package com.gavin.app.util.algorithm.sort;

import java.util.*;

/**
 * 找到一组数中最大或者最小的k个数
 * @author gavin
 * @date 2020/2/6 3:27 下午
 */
public class TopKSort {
    public static void main(String[] args) {
        int[] arr = {4,9,11,22,55,8,5,1,32,35,100};
        int[] res = topK(arr, 4);
        System.out.println(Arrays.toString(res));
    }

    public static ArrayList<Integer> GetLeastNumbers_Solution(int[] input, int k) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        if (k == 0 || k > input.length) return list;
        PriorityQueue<Integer> queue = new PriorityQueue(k, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });

        for (int i = 0; i < input.length; i++) {
            if (queue.size() != k) {
                queue.offer(input[i]);
            }
            else if (queue.peek() > input[i]) {
                Integer tmp = queue.poll();
                tmp = null;
                queue.offer(input[i]);
            }
        }

        for (Integer integer : queue) {
            list.add(integer);
        }

        return list;
    }

    /**
     *  找出最大的k个数
     * @return 索引
     */
    private static int[] topK(int[] arr, int k) {
        int low = 0, high = arr.length - 1;
        int index = partition(arr, low, high);
        while (index != k) {
            if (index > k) {
                high = index - 1;
                index = partition(arr, low, high);
            } else {
                low = index + 1;
                index = partition(arr, low, high);
            }
        }

        return Arrays.copyOf(arr, k);
    }

    private static int partition(int[] arr, int low, int high) {
        int midValue = arr[low];
        while (low < high) {
            while (low < high && arr[high] < midValue) {
                high--;
            }
            arr[low] = arr[high];
            while (low < high && arr[low] > midValue) {
                low++;
            }
            arr[high] = arr[low];
        }
        arr[low] = midValue;
        return low;
    }
}
