package com.gavin.app.json.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gavin
 * @date 2019-12-23 22:35
 */
public final class JsonArray extends JsonElement {
    private List<Object> list;

    public JsonArray() {
        list = new ArrayList<>();
    }
}
