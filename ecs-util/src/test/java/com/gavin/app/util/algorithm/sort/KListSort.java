package com.gavin.app.util.algorithm.sort;

import java.util.*;

/**
 * k个有序链表合并
 *
 * @author: Gavin
 * @date: 2021/3/4 12:29
 * @description:
 */
public class KListSort {
    public static void main(String[] args) {
        List<Integer> l1 = new ArrayList<>(Arrays.asList(5, 8, 10));
        List<Integer> l2 = new ArrayList<>(Arrays.asList(1, 3, 9));
        List<Integer> l3 = new ArrayList<>(Arrays.asList(2, 7, 11));
        List<List<Integer>> list = new ArrayList<List<Integer>>() {{
            add(l1);
            add(l2);
            add(l3);
        }};
        System.out.println(mergeKList(list));
    }


    public static List<Integer> mergeKList(List<List<Integer>> list) {
        Queue<List<Integer>> queue =new PriorityQueue<List<Integer>>(Comparator.comparingInt(a -> a.get(0)));
        List<Integer> result = new ArrayList<>();
        queue.addAll(list);
        while (!queue.isEmpty()) {
            List<Integer> tmp = queue.poll();
            if (!tmp.isEmpty()) {
                result.add(tmp.get(0));
                tmp.remove(0);
                if (!tmp.isEmpty()) {
                    queue.add(tmp);
                }
            }
        }
        return result;
    }
}