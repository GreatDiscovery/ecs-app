package com.gavin.app.redis;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

import java.util.concurrent.CountDownLatch;

/**
 * 分布式限流demo
 *
 * @author: Gavin
 * @date: 2021/2/18 17:48
 * @description:
 */
public class DistributeLimiter {
    public static void main(String[] args) throws InterruptedException {
        RedissonClient client = Redisson.create();
        RRateLimiter rateLimiter = client.getRateLimiter("myRateLimiter");
        // 初始化
        // 最大流速 = 每1秒钟产生1个令牌
        rateLimiter.trySetRate(RateType.OVERALL, 1, 1, RateIntervalUnit.SECONDS);

        int allThreadNum = 5;
        CountDownLatch latch = new CountDownLatch(allThreadNum);
        System.out.println("start-------------------------");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < allThreadNum; i++) {
            new Thread(() -> {
                rateLimiter.acquire(1);
                System.out.println(Thread.currentThread().getName());
                latch.countDown();
            }).start();
        }
        latch.await();
        System.out.println("Elapsed " + (System.currentTimeMillis() - startTime));
        System.out.println("end----------------------------");
        client.shutdown();
    }
}
