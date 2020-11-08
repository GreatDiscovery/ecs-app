package com.gavin.app.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试异步调用源码
 *
 * @author gavin
 * @date 2020/11/7 4:26 下午
 */
public class CompletableFutureTest {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        CompletableFuture cf = CompletableFuture.supplyAsync(() -> {
            try {
                //休眠200秒
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("supplyAsync " + Thread.currentThread().getName());
            return "hello ";
        },executorService).thenAccept(s -> {
            try {
                thenApply_test(s + "world");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        System.out.println(Thread.currentThread().getName());
        while (true) {
            if (cf.isDone()) {
                System.out.println("CompletedFuture...isDown");
                break;
            }
        }
        executorService.shutdown();
    }

    public static void thenApply_test(String s) {
        System.out.println(s);
    }

}
