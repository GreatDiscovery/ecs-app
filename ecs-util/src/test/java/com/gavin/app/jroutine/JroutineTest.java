package com.gavin.app.jroutine;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableRunnable;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.time.Duration;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * @author gavin
 * @date 2022/11/13 下午10:12
 * 对比一下java的线程执行和协程执行的耗时
 * https://www.51cto.com/article/701661.html
 */
public class JroutineTest {
    // 正常thread执行 50181ms
    public static void main(String[] args) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(10000);
        //记录系统线程数
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
            ThreadInfo[] threadInfo = threadBean.dumpAllThreads(false, false);
            System.out.println(threadInfo.length + " os thread");
        }, 1, 1, TimeUnit.SECONDS);
        long l = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(200);
        try {
            IntStream.range(0, 10000).forEach(i -> {
                executor.submit(() -> {
                    Thread.sleep(Duration.ofSeconds(1).toMillis());
                    System.out.println(i);
                    countDownLatch.countDown();
                    return i;
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        countDownLatch.await();
        scheduledExecutorService.shutdown();
        System.out.printf("耗时：%dms\n", System.currentTimeMillis() - l);
    }
}

// 需要增加JVM参数才能启动
class JVirtualThread {
    // 1416 ms左右就完成了，很快
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10000);
        long start = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            new Fiber<>(new SuspendableRunnable() {
                @Override
                public void run() throws SuspendExecution, InterruptedException {
                    Fiber.sleep(1000);
                    countDownLatch.countDown();
                }
            }).start();
        }

        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println("Fiber use:" + (end - start) + " ms");
    }

}