package com.gavin.app.util.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 三个线程同时打印26个字母
 *
 * @author gavin
 * @date 2021/4/17 下午9:24
 */
public class MultiThreadPrint {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        List<String> list = new ArrayList<>(26);
        for (int i = 97; i <= 122; i++) {
            list.add(String.valueOf((char)i));
        }
        for (int i = 0; i < list.size(); i++) {
            int tmp = i;
            executorService.execute(() -> {
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + ":" + list.get(tmp));
                } finally {
                    lock.unlock();
                }
            });
        }
        executorService.shutdown();
    }
}
