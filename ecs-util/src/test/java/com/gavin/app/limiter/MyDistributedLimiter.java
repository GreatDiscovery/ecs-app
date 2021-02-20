package com.gavin.app.limiter;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * fixme
 * 分布式限流封装
 * 这里有问题。假设1S产生1个令牌。
 * 如果100个请求同时获取，则只有1个能获取到，剩下的99个会放入定时任务中，每隔1S延时再次调用，直到所有的请求获取完毕
 *
 * @author: Gavin
 * @date: 2021/2/20 10:05
 * @description:
 */
public class MyDistributedLimiter {
    public static final String SCRIPT_LUA = "distributedLimiter.lua";
    public static final String SCRIPT = readScript();
    private static final Map<String, MyDistributedLimiter> LIMITS = new ConcurrentHashMap<>(10);
    private static final Map<String, String> SCRIPT_SHA = new ConcurrentHashMap<>(10);
    private String name;
    private double maxPermits;
    private JedisPool jedis;


    private MyDistributedLimiter(String name, double maxPermits, JedisPool jedis) {
        this.name = name;
        this.maxPermits = maxPermits;
        this.jedis = jedis;
        if (jedis == null) {
            throw new IllegalStateException("client can't be null!");
        }
        loadScript();
        trySetRate();
    }

    private static String readScript() {
        InputStream inputStream = MyDistributedLimiter.class.getClassLoader().getResourceAsStream(SCRIPT_LUA);
        Objects.requireNonNull(inputStream);
        String script = "";
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            script = bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
        }
        return script;
    }

    private void loadScript() {
        SCRIPT_SHA.computeIfAbsent(SCRIPT, k -> ((String)jedis.getResource().scriptLoad(SCRIPT)));
        System.out.println("SCRIPT_SHA=" + SCRIPT_SHA.values());
    }

    private void trySetRate() {
        String setRateScript = "redis.call('hsetnx', KEYS[1], 'rate', ARGV[1]);"
                + "redis.call('hsetnx', KEYS[1], 'interval', ARGV[2]);"
                + "return redis.call('hsetnx', KEYS[1], 'type', ARGV[3]);";
        jedis.getResource().eval(setRateScript, 1, name, String.valueOf((int)maxPermits), "1000", "0");
    }

    public static MyDistributedLimiter create(String key, double maxPermits, JedisPool jedis) {
        return LIMITS.computeIfAbsent(key, k -> new MyDistributedLimiter(k, maxPermits, jedis));
    }

    // fixme 有问题，Redisson在这里启动的定时任务不断地调用
    public double acquire() {
        String valueName = getValueName();
        String permitName = getPermitsName();
        String currentTime = String.valueOf(System.currentTimeMillis());
        String random = String.valueOf(ThreadLocalRandom.current().nextLong());
        System.out.println("evalsha " + SCRIPT_SHA.get(SCRIPT) + " 5 " + name + " " + valueName + " 1 " + permitName + " 1 " + "1 " + currentTime + " " + random);
        Long waitMicros = (Long) jedis.getResource().evalsha(SCRIPT_SHA.get(SCRIPT), Arrays.asList(name, getValueName(), "1", getPermitsName(), "1"), Arrays.asList("1", currentTime, random));
        if (waitMicros == null) {
            return 0;
        }
        if (waitMicros > 0) {
            sleepUninterruptibly(waitMicros, MILLISECONDS);
        }
        return waitMicros;
    }

    private void sleepUninterruptibly(long waitMicros, TimeUnit timeUnit) {
        boolean interrupted = false;
        try {
            long remainingNano = timeUnit.toNanos(waitMicros);
            long end = System.nanoTime() + remainingNano;
            while (true) {
                try {
                    NANOSECONDS.sleep(remainingNano);
                    return;
                } catch (InterruptedException e) {
                    interrupted = true;
                    remainingNano = end - System.nanoTime();
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private String getValueName() {
        return suffixName(getName(), "value");
    }

    private String getPermitsName() {
        return suffixName(getName(), "permits");
    }

    public static String suffixName(String name, String suffix) {
        if (name.contains("{")) {
            return name + ":" + suffix;
        }
        return "{" + name + "}:" + suffix;
    }
    private String getName() {
        return name;
    }

    public static void main(String[] args) throws Exception {
        JedisPool jedis = new JedisPool("127.0.0.1", 6379);
        MyDistributedLimiter limiter = create("l", 1, jedis);
        int nThread = 6;
        long start = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(nThread);
        ExecutorService executorService = Executors.newFixedThreadPool(nThread);
        for (int i = 0; i < nThread; i++) {
            executorService.submit(() -> {
                try {
                    System.out.println("------------------" + Thread.currentThread().getName() + ":" + limiter.acquire());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        System.out.println("elapsed=" + (System.currentTimeMillis() - start));
        jedis.close();
    }
}
