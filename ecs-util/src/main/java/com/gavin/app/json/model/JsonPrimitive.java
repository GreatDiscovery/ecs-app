package com.gavin.app.json.model;

import com.gavin.app.json.internal.LazilyParsedNumber;

/**
 * @author gavin
 * @date 2019-12-28 21:25
 */
public class JsonPrimitive extends JsonElement {
    private final Class<?>[] PRIMITIVE_TYPES = {byte.class, char.class, short.class, int.class, float.class, double.class, long.class, boolean.class,
            Byte.class, Character.class, Short.class, Integer.class, Float.class, Double.class, Boolean.class};
    Object value;

    public JsonPrimitive(Object primitive) {
        setValue(primitive);
    }

    public boolean isNumber() {
        return value instanceof Number;
    }

    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    public boolean isString() {
        return value instanceof String;
    }

    private void setValue(Object primitive) {
        if (isStringOrPrimitive(primitive)) {
            value = primitive;
        }
    }

    private boolean isStringOrPrimitive(Object o) {
        if (o == null) return false;
        if (o instanceof String) return true;
        Class<?> target = o.getClass();
        for (Class<?> primitive : PRIMITIVE_TYPES) {
            if (target.isAssignableFrom(primitive)) {
                return true;
            }
        }
        return false;
    }

    public Number getAsNumber() {
        return value instanceof String ? new LazilyParsedNumber((String) value) : (Number) value;
    }

    public Boolean getAsBoolean() {
        if (isBoolean()) {
            return (Boolean) value;
        }
        return Boolean.parseBoolean(getAsString());
    }

    public String getAsString() {
        if (isNumber()) {
            return getAsNumber().toString();
        } else if (isBoolean()) {
            return getAsBoolean().toString();
        } else {
            return (String) value;
        }
    }
}
