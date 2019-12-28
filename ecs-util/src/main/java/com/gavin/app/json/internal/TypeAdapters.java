package com.gavin.app.json.internal;

import com.gavin.app.json.model.JsonElement;
import com.gavin.app.json.model.JsonObject;
import com.gavin.app.json.model.JsonPrimitive;

import java.io.IOException;

/**
 * @author gavin
 * @date 2019-12-25 23:16
 */
public final class TypeAdapters {
    private TypeAdapters() {
        throw new UnsupportedOperationException();
    }

    public static final TypeAdapter<JsonElement> JSON_ELEMENT = new TypeAdapter() {
        @Override
        public JsonElement read(JsonReader in) throws IOException {
            switch (in.peek()) {
                case BEGIN_OBJECT:
                    JsonObject jsonObject = new JsonObject();
                    in.beginObject();
                    while (in.hasNext()) {
                        jsonObject.add(in.nextName(), read(in));
                    }
                    in.endObject();
                    return jsonObject;
                case STRING:
                    return new JsonPrimitive(in.nextString());
            }
            return null;
        }
    };
}
