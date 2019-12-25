package com.gavin.app.json.internal;

import com.gavin.app.json.model.JsonElement;

/**
 * @author: Gavin
 * @date: 2019/12/25 18:50
 * @description:
 */
public final class Streams {

    public static JsonElement parse(JsonReader reader) {
        boolean isEmpty = true;
        try {
            reader.peek();
            isEmpty = false;
            return TypeAdapters.JSON_ELEMENT.read(reader);
        } catch (Exception e) {

        }
        return null;
    }
}
