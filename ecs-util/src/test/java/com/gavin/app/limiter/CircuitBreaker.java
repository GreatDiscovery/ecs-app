package com.gavin.app.limiter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 断路器
 *
 * @author gavin
 * @date 2021/3/15 下午11:13
 */
@Data
@Slf4j
public class CircuitBreaker {
    // 最近N次
    private int limit;
    // 失败阈值
    private int failThreshold;
    // 失败次数
    private AtomicInteger fail = new AtomicInteger(0);
    // 总次数
    private AtomicInteger total = new AtomicInteger(0);
    // 延迟ms
    private long timeout;
    // 初始关闭
    private Status status = Status.CLOSED;
    // 上次打开的时间戳
    private long lastOpenTime;

    // 断路器状态
    public enum Status {
        OPEN {
            @Override
            public String toString() {
                return "open";
            }
        },
        CLOSED {
            @Override
            public String toString() {
                return "closed";
            }
        };
    }

    public boolean isClose() {
        return status == Status.CLOSED;
    }

    public boolean isOpen() {
        return status == Status.OPEN;
    }

    public void totalIncrement() {
        total.incrementAndGet();
    }

    public void fail() {
        fail.incrementAndGet();
        if (reachLimit()) {
            if (reachFailThreshold()) {
                lastOpenTime = System.currentTimeMillis();
                changeStatus(Status.OPEN);
            }
            reset();
        }
    }

    public boolean reachLimit() {
        return total.get() >= limit;
    }

    public boolean reachFailThreshold() {
        return fail.get() >= failThreshold;
    }

    public void fallback() {
        fail();
//        throw new CircuitBreakerFailException("断路器开启");
    }

    public synchronized void changeStatus(Status newStatus) {
        log.info("old:{} -> new:{}", status, newStatus);
        status = newStatus;
    }

    /**
     * 重置
     */
    public synchronized void reset() {
        total.set(0);
        fail.set(0);
    }

    /**
     * 延迟关闭
     *
     * @return
     */
    public boolean delayClose() {
        long delay = System.currentTimeMillis() - lastOpenTime;
        if (delay > timeout) {
            changeStatus(Status.CLOSED);
            return true;
        }
        return false;
    }
}

class CircuitBreakerRunner {
    public static void run(CircuitBreaker breaker, Runnable runnable) {
        try {
            if (breaker.isClose() || breaker.delayClose()) {
                runnable.run();
            }
        } catch (Exception e) {
            breaker.fallback();
        } finally {
            breaker.totalIncrement();
        }
    }

    public static void main(String[] args) {
        CircuitBreaker circuitBreaker = new CircuitBreaker();
        circuitBreaker.setLimit(5);
        circuitBreaker.setFailThreshold(2);
        circuitBreaker.setTimeout(5);

        Random random = new Random();
        for (int i = 1; i <= 100; i++) {
            int tmp = i;
            run(circuitBreaker, () -> {
                if (random.nextFloat() > 0.5) {
                    System.out.println("失败" + tmp);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    throw new RuntimeException("失败" + tmp);
                } else {
                    System.out.println("成功" + tmp);
                }
            });
        }
    }
}

class CircuitBreakerFailException extends RuntimeException {
    public CircuitBreakerFailException() {
        super();
    }

    public CircuitBreakerFailException(String message) {
        super(message);
    }

    public CircuitBreakerFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public CircuitBreakerFailException(Throwable cause) {
        super(cause);
    }

    protected CircuitBreakerFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}