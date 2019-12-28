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
}
