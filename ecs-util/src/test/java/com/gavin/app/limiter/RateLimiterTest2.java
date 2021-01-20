package com.gavin.app.limiter;

import com.google.common.util.concurrent.RateLimiter;

/**
 * @author gavin
 * @date 2021/1/20 下午11:37
 */
public class RateLimiterTest2 {
    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(5);
        System.out.println(rateLimiter.acquire());
        System.out.println(rateLimiter.acquire());
        System.out.println(rateLimiter.acquire());
        System.out.println(rateLimiter.acquire());
        System.out.println(rateLimiter.acquire());
        System.out.println(rateLimiter.acquire());
    }
}
