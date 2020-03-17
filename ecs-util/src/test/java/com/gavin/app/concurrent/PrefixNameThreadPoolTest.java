package com.gavin.app.concurrent;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 创建有业务含义名称的线程
 * @author gavin
 * @date 2020/3/14 4:49 下午
 */
public class PrefixNameThreadPoolTest {
    public static void main(String[] args) {
        PrefixNameThreadFactory factory = new PrefixNameThreadFactory("my");
        BlockingQueue blockingQueue = new ArrayBlockingQueue(10);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,
                5,
                0,
                TimeUnit.MILLISECONDS,
                blockingQueue,
                factory);
        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.submit(() -> {
                System.out.println(Thread.currentThread().getName() + "|" + Thread.currentThread().getThreadGroup().getName());
            });
        }
        threadPoolExecutor.shutdown();
    }
}

/**
 * 可以自定义前缀名称的线程工厂
 */
class PrefixNameThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    public PrefixNameThreadFactory(String prefix) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        namePrefix = prefix +
                poolNumber.getAndIncrement() +
                "-thread-";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}