package com.gavin.app.json.internal;


import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;

/**
 * @author gavin
 * @date 2019-12-29 17:47
 */
public class JsonWriter implements Closeable, Flushable {

    private Writer out;
    private int[] stack = new int[32];
    int stackSize = 0;
    {
        stack[stackSize++] = JsonScope.EMPTY_DOCUMENT;
    }

    public JsonWriter(Writer out) {
        if (out == null) {
            throw new NullPointerException("out == null");
        }
        this.out = out;
    }

    public JsonWriter beginObject() throws IOException {
        return open(JsonScope.EMPTY_OBJECT, "{");
    }

    public JsonWriter endObject() throws IOException {
        return null;
    }

    public JsonWriter open(int empty, String openBracket) throws IOException {
        beforeValue();
        push(empty);
        out.write(openBracket);
        return this;
    }

    private void beforeValue() {
        int p = peek();
        switch (p) {
            case JsonScope.EMPTY_DOCUMENT:
                replaceTop(JsonScope.NONEMPTY_OBJECT);
                break;
        }
    }

    private void replaceTop(int topOfStack) {
        stack[stackSize - 1] = topOfStack;
    }

    private int peek() {
       if (stackSize == 0) {
           throw new IllegalStateException("JsonWriter is closed.");
       }
       return stack[stackSize - 1];
    }

    private void push(int newTop) {
        if (stackSize == stack.length) {
            resize();
        }
        stack[stackSize++] = newTop;
    }

    private void resize() {
        int[] newStack = new int[stackSize * 2];
        System.arraycopy(stack, 0, newStack, 0, stackSize);
        stack = newStack;
    }

    @Override
    public void flush() throws IOException {
        if (out == null) {
            throw new IOException();
        }
        out.flush();
    }

    @Override
    public void close() throws IOException {
        if (out == null) {
            throw new IOException();
        }
        out.close();
    }

}
