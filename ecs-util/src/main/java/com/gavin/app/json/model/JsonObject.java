package com.gavin.app.json.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gavin
 * @date 2019-12-23 22:35
 */
public final class JsonObject<K, V> extends JsonElement {
    private Map<K, V> map = new HashMap<>();

    public JsonObject() {
        this.map = new HashMap<>();
    }

    public V put(K key, V value) {
       return map.put(key, value);
    }

    public V add(K key, V value) {
        return this.put(key, value);
    }
    @Override
    public String toString() {
        return map.toString();
    }
}
