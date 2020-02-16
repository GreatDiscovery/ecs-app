package com.gavin.app.util.algorithm.map;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Google的线程安全的hashMap
 * @author gavin
 * @date 2020/2/12 10:21 下午
 */
public class ConcurrentLinkedHashMap<K, V> extends AbstractMap<K, V> implements  Serializable {

    static final int NCPU = Runtime.getRuntime().availableProcessors();
    static final int NUMBER_OF_READ_BUFFERS = ceilingNextPowerOfTwo(NCPU);
    static int ceilingNextPowerOfTwo(int x) {
        return 1 << (Integer.SIZE - Integer.numberOfLeadingZeros(x - 1));
    }

    final int concurrencyLevel;
    final AtomicLong capacity;
    final ConcurrentMap<K,Node<K,V>> data;

    private ConcurrentLinkedHashMap(Builder<K, V> builder) {
        concurrencyLevel = builder.concurrentLevel;
        capacity = new AtomicLong(builder.capacity);
        data = new ConcurrentHashMap<K,Node<K,V>>(builder.initialCapacity, 0.75f, builder.concurrentLevel);
    }

    /** Ensures that the object is not null. */
    static void checkNotNull(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
    }

    /** Ensures that the argument expression is true. */
    static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    /** Ensures that the state expression is true. */
    static void checkState(boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return null;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {

    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {

    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return null;
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    public boolean remove(Object key, Object value) {
        return false;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return false;
    }

    enum DiscardListener implements EvictionListener<Object,Object> {
        INSTANCE;

        @Override
        public void onEviction(Object k, Object v) {

        }
    }

    static final class Node<K,V> {

    }

    public static final class Builder<K, V> {
        static final int DEFAULT_CONCURRENT_LEVEL = 16;
        static final int DEFAULT_INITIAL_CAPACITY = 16;

        EntryWeigher<? super K, ? super V> weigher;

        int concurrentLevel;
        int initialCapacity;
        long capacity;
        EvictionListener<K,V> listener;

        public Builder() {
            capacity = -1;
            weigher = Weighers.entrySingleton();
            concurrentLevel = DEFAULT_CONCURRENT_LEVEL;
            initialCapacity = DEFAULT_INITIAL_CAPACITY;
            listener = (EvictionListener<K, V>) DiscardListener.INSTANCE;
        }

        public ConcurrentLinkedHashMap<K, V> build() {
            return new ConcurrentLinkedHashMap<>(this);
        }
    }

    public static void main(String[] args) {
    }
}
