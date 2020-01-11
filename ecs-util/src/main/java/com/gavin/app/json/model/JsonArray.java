package com.gavin.app.json.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gavin
 * @date 2019-12-23 22:35
 */
public final class JsonArray extends JsonElement {
    private List<JsonElement> elements;

    public JsonArray() {
        elements = new ArrayList<>();
    }

    public void add(JsonElement e) {
        if (e == null) {
            e = JsonNull.INSTANCE;
        }
        elements.add(e);
    }
}
