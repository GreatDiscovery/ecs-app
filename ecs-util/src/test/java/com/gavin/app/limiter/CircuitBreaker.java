package com.gavin.app.limiter;

import lombok.Data;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 断路器
 *
 * @author gavin
 * @date 2021/3/15 下午11:13
 */
@Data
public class CircuitBreaker {
    // 最近N次
    private int limit;
    // 失败阈值
    private int failThreshold;
    // 失败次数
    private AtomicInteger fail;
    // 总次数
    private AtomicInteger total;
    // 延迟ms
    private long timeout;
    // 初始关闭
    private Status status = Status.CLOSED;
    // 上次打开的时间戳
    private long lastOpenTime;

    // 断路器状态
    public enum Status {
        OPEN,
        CLOSED;
    }

    public boolean isClose() {
        return status == Status.CLOSED;
    }

    public boolean isOpen() {
        return status == Status.OPEN;
    }

    public void increment() {
        total.incrementAndGet();
    }

    public void fail() {
        fail.incrementAndGet();
        total.incrementAndGet();
        if (reachLimit()) {
            if (reachFailThreshold()) {
                lastOpenTime = System.currentTimeMillis();
                changeStatus(Status.OPEN);
            }
            reset();
        }
    }

    /**
     * 达到总数限制
     *
     * @return
     */
    public boolean reachLimit() {
        return total.get() > limit;
    }

    public boolean reachFailThreshold() {
        return fail.get() > failThreshold;
    }

    public void fallback() {
        fail();
        throw new CircuitBreakerFailException("断路器开启");
    }

    public synchronized void changeStatus(Status newStatus) {
        synchronized (this) {
            status = newStatus;
        }
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
    public static void run(CircuitBreaker breaker, Callable callable) {
        try {
            if (breaker.isClose() || breaker.delayClose()) {
                callable.call();
            } else {
                breaker.fallback();
            }
        } catch (Exception e) {
            breaker.fallback();
        } finally {
            breaker.increment();
        }
    }

    public static void main(String[] args) {
        while (true) {

        }
    }
}

class CircuitBreakerFailException extends RuntimeException {￿
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