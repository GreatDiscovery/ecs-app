package com.gavin.app.json.parser;

import com.gavin.app.json.exception.JsonParseException;
import com.gavin.app.json.internal.Streams;
import com.gavin.app.json.model.JsonElement;
import com.gavin.app.json.internal.JsonReader;

import java.io.Reader;
import java.io.StringReader;

/**
 * @author: Gavin
 * @date: 2019/12/25 18:34
 * @description:
 */
public class JsonParser {

    public JsonElement parse(String json) {
        return this.parse(new StringReader(json));
    }

    public JsonElement parse(Reader reader) throws JsonParseException {
        try {
            JsonReader jsonReader = new JsonReader(reader);
            JsonElement jsonObject = parse(jsonReader);
            return jsonObject;
        } catch (Exception e) {
            throw new JsonParseException("json转换异常");
        }
    }

    public JsonElement parse(JsonReader jsonReader) throws JsonParseException {
        try {
            return Streams.parse(jsonReader);
        } catch (Exception e) {
            throw new JsonParseException("json转换异常");
        }
    }
}
