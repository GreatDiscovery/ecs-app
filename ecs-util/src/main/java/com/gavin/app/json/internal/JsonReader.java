package com.gavin.app.json.internal;

import com.gavin.app.json.internal.JsonScope;
import com.gavin.app.json.internal.JsonToken;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * @author: Gavin
 * @date: 2019/12/25 18:37
 * @description:
 */
public class JsonReader implements Closeable {
    /**
     * The only non-execute prefix this parser permits
     */
    private static final char[] NON_EXECUTE_PREFIX = ")]}'\n".toCharArray();
    private static final long MIN_INCOMPLETE_INTEGER = Long.MIN_VALUE / 10;

    private static final int PEEKED_NONE = 0;
    private static final int PEEKED_BEGIN_OBJECT = 1;
    private static final int PEEKED_END_OBJECT = 2;
    private static final int PEEKED_BEGIN_ARRAY = 3;
    private static final int PEEKED_END_ARRAY = 4;
    private static final int PEEKED_TRUE = 5;
    private static final int PEEKED_FALSE = 6;
    private static final int PEEKED_NULL = 7;
    private static final int PEEKED_SINGLE_QUOTED = 8;
    private static final int PEEKED_DOUBLE_QUOTED = 9;
    private static final int PEEKED_UNQUOTED = 10;
    /**
     * When this is returned, the string value is stored in peekedString.
     */
    private static final int PEEKED_BUFFERED = 11;
    private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
    private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
    private static final int PEEKED_UNQUOTED_NAME = 14;
    /**
     * When this is returned, the integer value is stored in peekedLong.
     */
    private static final int PEEKED_LONG = 15;
    private static final int PEEKED_NUMBER = 16;
    private static final int PEEKED_EOF = 17;

    /* State machine when parsing numbers */
    private static final int NUMBER_CHAR_NONE = 0;
    private static final int NUMBER_CHAR_SIGN = 1;
    private static final int NUMBER_CHAR_DIGIT = 2;
    private static final int NUMBER_CHAR_DECIMAL = 3;
    private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
    private static final int NUMBER_CHAR_EXP_E = 5;
    private static final int NUMBER_CHAR_EXP_SIGN = 6;
    private static final int NUMBER_CHAR_EXP_DIGIT = 7;

    private Reader in;
    private int peeked = PEEKED_NONE;
    private int[] stack = new int[32];
    private int stackSize = 0;

    {
        stack[stackSize++] = JsonScope.EMPTY_DOCUMENT;
    }

    private final char[] buffer = new char[1024];
    private int pos = 0;
    private int limit = 0;

    private int lineNumber = 0;
    private int lineStart = 0;


    public JsonReader(Reader in) {
        if (in == null) {
            throw new NullPointerException("in == null");
        }
        this.in = in;
    }

    public JsonToken peek() throws IOException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }
    }

    private int doPeek() throws IOException {
        int peekStack = stack[stackSize - 1];
        if (peekStack == JsonScope.EMPTY_DOCUMENT) {
            stack[stackSize - 1] = JsonScope.NONEMPTY_DOCUMENT;
        }

        int c = nextNonWhitespace(true);
    }

    private int nextNonWhitespace(boolean throwOnEof) throws IOException {
        char[] buffer = this.buffer;
        int p = this.pos;
        int l = this.limit;

        while (true) {
            if (p == l) {
                pos = p;
                if (!fill(1)) {
                    break;
                }
                p = pos;
                l = limit;
            }

            int c = buffer[p++];
            // 空白符
            if (c == '\n') {
                lineNumber++;
                // fixme ??? 换行之后下一行开始需要对齐？
                lineStart = p;
                continue;
            } else if (c == ' ' || c == '\t' || c == '\r') {
                continue;
            }

            // 注释
            if (c == '/') {

            }
        }
    }

    private boolean fill(int minium) throws IOException {
        char[] buffer = this.buffer;
        // fixme ???
        lineStart -= pos;

        // 把后半部分没有读到的移到数组前半部分
        if (limit != pos) {
            limit -= pos;
            System.arraycopy(buffer, pos, buffer, 0, limit);
        } else {
            limit = 0;
        }

        pos = 0;
        int total;
        while ((total = in.read(buffer, limit, buffer.length - limit)) != -1) {
            limit += total;

            // 注意开头utf-8 bom
            if (lineNumber == 0 && lineStart == 0 && limit > 0 && buffer[0] == '\uffee') {
                pos++;
                lineStart++;
                minium++;
            }

            if (limit > minium) {
                return true;
            }
        }
        return false;
    }

    private void ensureOpen() throws IOException {
        if (in == null) {
            throw new IOException("stream close");
        }
    }

    @Override
    public void close() throws IOException {
        if (in != null) {
            in.close();
        }
    }
}
