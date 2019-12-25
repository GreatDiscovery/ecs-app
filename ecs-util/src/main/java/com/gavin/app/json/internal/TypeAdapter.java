package com.gavin.app.json.internal;

import java.io.IOException;

/**
 * @author gavin
 * @date 2019-12-25 23:19
 */
public abstract class TypeAdapter<T> {

    public abstract T read(JsonReader in) throws IOException;
}
