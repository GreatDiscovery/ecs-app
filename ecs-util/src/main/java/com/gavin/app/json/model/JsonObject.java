package com.gavin.app.json.model;

import com.gavin.app.json.internal.LinkedTreeMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author gavin
 * @date 2019-12-23 22:35
 */
public final class JsonObject extends JsonElement {
    private Map<String, JsonElement> members = new LinkedTreeMap<>();

    public void add(String key, JsonElement value) {
        if (value == null) {
            value = JsonNull.INSTANCE;
        }
        members.put(key, value);
    }

    public Set<Map.Entry<String, JsonElement>> entrySet() {
        return members.entrySet();
    }
}
