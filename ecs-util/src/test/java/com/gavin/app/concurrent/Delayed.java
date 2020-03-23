package com.gavin.app.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * @author gavin
 * @date 2020/3/23 9:53 下午
 */
public interface Delayed extends Comparable<Delayed> {
    long getDelay(TimeUnit unit);
}
