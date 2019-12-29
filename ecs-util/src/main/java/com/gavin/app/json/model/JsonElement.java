package com.gavin.app.json.model;

import com.gavin.app.json.internal.JsonWriter;
import com.gavin.app.json.internal.Streams;

import java.io.IOException;
import java.io.StringWriter;

/**
 * @author: Gavin
 * @date: 2019/12/25 18:55
 * @description:
 */
public abstract class JsonElement {

    public boolean isJsonObject() {
        return this instanceof JsonObject;
    }

    @Override
    public String toString() {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        try {
            Streams.write(this, jsonWriter);
        } catch (IOException e) {
            throw new IllegalStateException("打印错误");
        }
        return stringWriter.toString();
    }
}
