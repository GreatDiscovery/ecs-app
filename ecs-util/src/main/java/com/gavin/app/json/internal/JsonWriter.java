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

    private static final String[] REPLACEMENT_CHARS;

    static {
        REPLACEMENT_CHARS = new String[128];
        for (int i = 0; i <= 0x1f; i++) {
            REPLACEMENT_CHARS[i] = String.format("\\u%04x", (int) i);
        }
        REPLACEMENT_CHARS['"'] = "\\\"";
        REPLACEMENT_CHARS['\\'] = "\\\\";
        REPLACEMENT_CHARS['\t'] = "\\t";
        REPLACEMENT_CHARS['\b'] = "\\b";
        REPLACEMENT_CHARS['\n'] = "\\n";
        REPLACEMENT_CHARS['\r'] = "\\r";
        REPLACEMENT_CHARS['\f'] = "\\f";
    }

    // 用来作为缩进符号的，比如空白符
    private String indent;

    private String separator = ":";

    public JsonWriter(Writer out) {
        if (out == null) {
            throw new NullPointerException("out == null");
        }
        this.out = out;
    }

    public final void setIndent(String indent) {
        if (indent.length() == 0) {
            this.indent = null;
            separator = ":";
        } else {
            this.indent = indent;
            separator = ":";
        }
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
        return close(JsonScope.EMPTY_OBJECT, JsonScope.NONEMPTY_OBJECT, "}");
    }

    public JsonWriter open(int empty, String openBracket) throws IOException {
        beforeValue();
        push(empty);
        out.write(openBracket);
        return this;
    }

    public JsonWriter close(int empty, int notEmpty, String closeBracket) throws IOException {
        int context = peek();
        if (context != empty && context != notEmpty) {
            throw new IllegalStateException("Nesting problem");
        }
        if (deferredName != null) {
            throw new IllegalStateException("Dangling name " + deferredName);
        }

        stackSize--;
        if (context == notEmpty) {
            newLine();
        }
        out.write(closeBracket);
        return this;
    }

    private void beforeValue() throws IOException {
        int p = peek();
        switch (p) {
            case JsonScope.EMPTY_DOCUMENT:
                replaceTop(JsonScope.NONEMPTY_DOCUMENT);
                break;
            case JsonScope.DANGLING_NAME:
                out.write(separator);
                replaceTop(JsonScope.NONEMPTY_OBJECT);
                break;
            default:
                throw new IllegalStateException("nesting problem");

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

    private void string(String value) throws IOException {
        String[] replacements = REPLACEMENT_CHARS;
        out.write('\"');
        // 写的时候需要考虑转义的问题
        out.write(value);
        out.write('\"');
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