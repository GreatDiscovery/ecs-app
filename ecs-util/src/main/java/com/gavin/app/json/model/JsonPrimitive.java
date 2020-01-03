package com.gavin.app.json.model;

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
        // todo String -> Number
        return value instanceof String ? null : (Number) value;
    }

    public Boolean getAsBoolean() {
        return null;
    }

    public String getAsString() {
        return (String) value;
    }
}
