package com.gavin.app.util.algorithm;

import java.util.LinkedHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author gavin
 * @date 2021/5/15 下午5:03
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static final int DEFAULT_MAX_CAPACITY = 1000;
    private final Lock lock = new ReentrantLock();
    private volatile int maxCapacity;

    public LRUCache() {
        this(DEFAULT_MAX_CAPACITY);
    }

    public LRUCache(int maxCapacity) {
        super(16, DEFAULT_LOAD_FACTOR, true);
        this.maxCapacity = maxCapacity;
    }

    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
        return size() > maxCapacity;
    }

    @Override
    public boolean containsKey(Object key) {
        lock.lock();
        try {
            return super.containsKey(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V get(Object key) {
        lock.lock();
        try {
            return super.get(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        lock.lock();
        try {
            return super.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V remove(Object key) {
        lock.lock();
        try {
            return super.remove(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        lock.lock();
        try {
            return super.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            super.clear();
        } finally {
            lock.unlock();
        }
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public static void main(String[] args) {
        LinkedHashMap<String, String> map = new LRUCache<>(3);
        map.put("1", "gavin");
        map.put("2", "gavin");
        map.put("3", "gavin");
        map.put("4", "gavin");
        map.put("5", "gavin");
        map.put("6", "gavin");
        map.forEach((k, v) -> System.out.println("k=" + k + " v=" + v));
        map.get("5");
        System.out.println("lru改变了顺序");
        map.forEach((k, v) -> System.out.println("k=" + k + " v=" + v));
    }
}
