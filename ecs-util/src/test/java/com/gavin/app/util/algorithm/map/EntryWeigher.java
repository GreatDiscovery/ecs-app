package com.gavin.app.util.algorithm.map;

/**
 * @author gavin
 * @date 2020/2/15 10:41 上午
 */
public interface EntryWeigher<K, V> {
    int weightOf(K k, V v);
}
