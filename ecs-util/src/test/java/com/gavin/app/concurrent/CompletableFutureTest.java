package com.gavin.app.concurrent;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

/**
 * 测试异步调用源码
 *
 * @author gavin
 * @date 2020/11/7 4:26 下午
 */
public class CompletableFutureTest {

    // 测试并行调用
    @Test
    public void testParallel() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        System.out.println("start");
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("start f1");
            try {
                Thread.sleep(5000);
                System.out.println("end f1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
            }
            return "hello";
        }, executorService);

        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("start f2");
            try {
                Thread.sleep(2000);
                System.out.println("end f2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
            }
            return " world";
        }, executorService);

        CompletableFuture<Void> f12 = f1.thenAcceptBothAsync(f2, (s, s2) -> System.out.println(s + s2), executorService);
        CompletableFuture.allOf(f12).join();
        System.out.println("end");

    }
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
