package com.gavin.app.util.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 找到一组数中最大或者最小的k个数
 * @author gavin
 * @date 2020/2/6 3:27 下午
 */
public class TopKSort {
    public static void main(String[] args) {
        int[] arr = {4,9,11,22,55,8,5,1,32,35};
        ArrayList<Integer> list = GetLeastNumbers_Solution(arr, 4);
        System.out.println(list.toString());
    }

    public static ArrayList<Integer> GetLeastNumbers_Solution(int [] input, int k) {
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
}
