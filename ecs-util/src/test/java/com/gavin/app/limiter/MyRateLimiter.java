package com.gavin.app.limiter;

import org.springframework.util.StopWatch;

/**
 * 手动实现rateLimiter
 *
 * @author gavin
 * @date 2021/1/20 下午10:23
 */
public class MyRateLimiter {
    private volatile Object lock = new Object();
    private double maxPermits;
    private double rate;
    private volatile double storePermits = 0;
    private long nextTimeWindow = 0;

    public MyRateLimiter(double permits) {
        this.rate = 1000 / permits;
        reSyn(System.currentTimeMillis());
        this.maxPermits = permits;
    }

    public double acquire() {
        double timeToWait = acquire(1);
        if (timeToWait > 0) {
            sleepWithoutInterrupt(timeToWait);
        }
        return timeToWait / 1000;
    }

    public double acquire(double permits) {
        synchronized (lock) {
            reSyn(System.currentTimeMillis());
            double require = permits;
            double have = storePermits;
            double storeToSpend = Math.min(require, have);
            double lack = require - storeToSpend;
            storePermits -= storeToSpend;
            double timeToWait = lack * rate;
            nextTimeWindow += timeToWait;
            return timeToWait;
        }
    }

    public void sleepWithoutInterrupt(double time) {
        long end = System.currentTimeMillis();
        long remainingTime = (long) time;
        boolean interrupt = false;
        while (true) {
            try {
//                System.out.println("睡眠" + remainingTime);
                Thread.sleep(remainingTime);
                return;
            } catch (InterruptedException e) {
                remainingTime = end - System.currentTimeMillis();
                interrupt = true;
            } finally {
                if (interrupt) {
                    Thread.currentThread().interrupt();
                }
            }
        }

    }

    public void reSyn(long nowTime) {
        if (nowTime > nextTimeWindow) {
            storePermits = Math.min(maxPermits, storePermits + (nowTime - nextTimeWindow) / rate);
            nextTimeWindow = nowTime;
        }
    }

    public static void main(String[] args) {
        MyRateLimiter limiter = new MyRateLimiter(5);
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
    }
}
