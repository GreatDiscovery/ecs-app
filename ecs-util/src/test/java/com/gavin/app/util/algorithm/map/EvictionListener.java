package com.gavin.app.util.algorithm.map;

/**
 * @author gavin
 * @date 2020/2/15 11:03 上午
 */
public interface EvictionListener<K,V> {
    public void onEviction(K k, V v);
}
