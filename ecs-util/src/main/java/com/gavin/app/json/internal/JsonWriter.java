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

    private String deferredName;

    int stackSize = 0;
    {
        stack[stackSize++] = JsonScope.EMPTY_DOCUMENT;
    }

    // 用来作为缩进符号的，比如空白符
    private String indent;

    public JsonWriter(Writer out) {
        if (out == null) {
            throw new NullPointerException("out == null");
        }
        this.out = out;
    }

    public JsonWriter beginObject() throws IOException {
        return open(JsonScope.EMPTY_OBJECT, "{");
    }

    public JsonWriter name(String name) {
        if (name == null) {
            throw new NullPointerException("name == null");
        }
        if (deferredName != null) {
            throw new IllegalStateException("deferredName is not null");
        }
        if (stackSize == 0) {
            throw new IllegalStateException("JsonWriter is closed");
        }
        deferredName = name;
        return this;
    }

    public JsonWriter endObject() throws IOException {
        return close(JsonScope.CLOSED, "}");
    }

    public JsonWriter open(int empty, String openBracket) throws IOException {
        beforeValue();
        push(empty);
        out.write(openBracket);
        return this;
    }

    public JsonWriter close(int empty, String closeBracket) {
        return null;
    }

    private void beforeValue() {
        int p = peek();
        switch (p) {
            case JsonScope.EMPTY_DOCUMENT:
                replaceTop(JsonScope.NONEMPTY_DOCUMENT);
                break;
        }
    }

    public void writeDeferredName() throws IOException {
        if (deferredName != null) {
            beforeName();
            string(deferredName);
            deferredName = null;
        }
    }

    private void beforeName() throws IOException {
        int context = peek();
        if (context == JsonScope.NONEMPTY_OBJECT) {
            out.write(',');
        } else if (context != JsonScope.EMPTY_OBJECT) {
            throw new IllegalStateException("nesting problem.");
        }

        // context == EMPTY_OBJECT
        newLine();
        replaceTop(JsonScope.DANGLING_NAME);
    }

    private void newLine() throws IOException {
        // 如果不需要缩进的话，换行也就没有必要了
        if (indent == null) return;
        out.write('\n');

        // 根据深度缩进
        for (int i = 0, size = stackSize; i < size; i++) {
            out.write(indent);
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

    public JsonWriter value(String value) throws IOException {
        if (value == null) {
           return null;
        }

        writeDeferredName();
        beforeValue();
        string(value);
        return this;
    }

    public void value(Boolean value) {

    }

    public void value(Number value) {

    }
}
