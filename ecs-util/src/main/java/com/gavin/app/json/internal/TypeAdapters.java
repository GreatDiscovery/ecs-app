package com.gavin.app.json.internal;

import com.gavin.app.json.model.JsonArray;
import com.gavin.app.json.model.JsonElement;
import com.gavin.app.json.model.JsonObject;
import com.gavin.app.json.model.JsonPrimitive;

import java.io.IOException;
import java.util.Map;

/**
 * @author gavin
 * @date 2019-12-25 23:16
 */
public final class TypeAdapters {
    private TypeAdapters() {
        throw new UnsupportedOperationException();
    }

    public static final TypeAdapter<JsonElement> JSON_ELEMENT = new TypeAdapter<JsonElement>() {
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
                case BEGIN_ARRAY:
                    JsonArray jsonArray = new JsonArray();
                    in.beginArray();
                    while (in.hasNext()) {
                        jsonArray.add(read(in));
                    }
                    in.endArray();
                    return jsonArray;
                case STRING:
                    return new JsonPrimitive(in.nextString());
                case NUMBER:
                    String number = in.nextString();
                    return new JsonPrimitive(new LazilyParsedNumber(number));
                case BOOLEAN:
                    return new JsonPrimitive(in.nextBoolean());
            }
            return null;
        }

        @Override
        public void write(JsonWriter out, JsonElement value) throws IOException {
            if (value.isJsonObject()) {
                out.beginObject();
                for (Map.Entry<String, JsonElement> e : value.getAsJsonObject().entrySet()) {
                    out.name(e.getKey());
                    write(out, e.getValue());
                }
                out.endObject();
            } else if (value.isJsonPrimitive()) {
                JsonPrimitive primitive = value.getAsJsonPrimitive();
                if (primitive.isNumber()) {
                    out.value(primitive.getAsNumber());
                } else if (primitive.isBoolean()) {
                    out.value(primitive.getAsBoolean());
                } else {
                    out.value(primitive.getAsString());
                }
            } else if (value.isJsonArray()) {
                out.beginArray();
                for (JsonElement e : value.getAsJsonArray()) {
                    write(out, e);
                }
                out.endArray();
            }
        }
    };

}
