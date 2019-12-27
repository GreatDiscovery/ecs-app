package com.gavin.app.json.internal;

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

        switch (p) {
            case PEEKED_BEGIN_OBJECT:
                return JsonToken.BEGIN_OBJECT;
        }
        return null;
    }

    private int doPeek() throws IOException {
        int peekStack = stack[stackSize - 1];
        if (peekStack == JsonScope.EMPTY_DOCUMENT) {
            stack[stackSize - 1] = JsonScope.NONEMPTY_DOCUMENT;
        } else if (peekStack == JsonScope.EMPTY_OBJECT || peekStack == JsonScope.NONEMPTY_OBJECT)  {
            stack[stackSize - 1] = JsonScope.DANGLING_NAME;
            if (peekStack == JsonScope.NONEMPTY_OBJECT) {
                int c = nextNonWhitespace(true);
                switch (c) {
                    case '':
                    case '':
                    case '':
                }

            }
        }

        int c = nextNonWhitespace(true);
        switch (c) {
            case '{':
                return peeked = PEEKED_BEGIN_OBJECT;
            case '"':
                return peeked = PEEKED_DOUBLE_QUOTED;
        }
        return -1;
    }

    private int nextNonWhitespace(boolean throwOnEof) throws IOException {
        char[] buffer = this.buffer;
        int p = this.pos;
        int l = this.limit;

        while (true) {
            // buffer已经读完了
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

            } else if (c == '#') {

            }

            pos = p;
            return c;
        }

        if (throwOnEof) {
            throw new IOException("End of input" + locationString());
        } else {
            return -1;
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

    private String locationString() {
        int line = lineNumber + 1;
        int column = pos - lineStart + 1;
        return "at line " + line + "at column " + column;
    }

    public void beginObject() throws IOException {
        int p = this.peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }

        if (p == PEEKED_BEGIN_OBJECT) {
            push(JsonScope.EMPTY_OBJECT);
            peeked = PEEKED_NONE;
        } else {
            throw new IllegalStateException("Expected BEGIN_OBJECT but was " + peek() +
                    " at line " + getLineNumber() + " at column " + getLineColumn());
        }
    }

    public void endObject() throws IOException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }

        if (p == PEEKED_END_OBJECT) {
            stackSize--;
            peeked = PEEKED_NONE;
        } else {
            throw new IllegalStateException("Expected END_OBJECT but was " + peek()
                    + " at line " + getLineNumber() + " column " + getLineColumn());
        }
    }

    public boolean hasNext() throws IOException {
        int p = this.peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }
        return p != PEEKED_END_OBJECT && p != PEEKED_END_ARRAY;
    }

    public String nextName() throws IOException {
        int p = this.peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }

        String result;
        if (p == PEEKED_DOUBLE_QUOTED) {
            result = nextQuotedValue('"');
        } else {
            throw new IOException("invalid stream");
        }
        peeked = PEEKED_NONE;
        return result;
    }

    private String nextQuotedValue(char quoted) throws IOException {
        char[] buffer = this.buffer;
        StringBuilder builder = new StringBuilder();

        while (true) {
            int p = pos;
            int l = limit;
            // 这里用start记录第一个字符，有可能不是字符串
            int start = p;

            while (p < l) {
                char c = buffer[pos++];

                if (c == quoted) {
                    builder.append(buffer, start, pos - start - 1);
                    return builder.toString();
                } else if (c == '\\') {
                    pos = p;
                    builder.append(buffer, start, pos - start - 1);
                    builder.append(readEscapeCharacter());
                    p = pos;
                    l = limit;
                    start = p;
                } else if (c == '\n') {
                    lineNumber++;
                    lineStart = p;
                }
            }

            pos = p;
            if (!fill(1)) {
                throw new IOException("填充失败");
            }
        }
    }

    private char readEscapeCharacter() throws IOException {
        if (pos == limit && !fill(1)) throw new IOException("Unterminated escape sequence");
        char escape = buffer[pos++];
        switch (escape) {
            case 'n':
                return '\n';
            case 't':
                return '\t';
            case 'r':
                return '\r';
            case 'u':
                if (pos + 4 > limit && !fill(4)) {
                    throw new IOException("Unterminated escape sequence");
                }
                char result = 0;
                for (int i = pos, end = i + 4; i < end; i++) {
                    result <<= 4;
                    char c = buffer[i];
                    if (c >= '0' && c <= '9') {
                        result += (c - '0');
                    } else if (c >= 'a' && c <= 'f') {
                        // a == 10, b == 11 ... f == 15
                        result += (c - 'a' + 10);
                    } else if (c >= 'A' && c <= 'F') {
                        result += (c - 'A' + 10);
                    } else {
                        throw new IOException("Unterminated escape sequence");
                    }
                }
                pos += 4;
                return result;

            case '"':
            case '\\':
            case '/':
                return escape;

            default:
                throw new IOException("Invalid escape sequence");
        }
    }

    @Override
    public void close() throws IOException {
        if (in != null) {
            in.close();
        }
    }

    private void push(int newTop) {
        if (stackSize == stack.length) {
            resize();
        }
        stack[stackSize++] = newTop;
    }

    private void resize() {
        if (stackSize == stack.length) {
            int[] newStack = new int[stackSize * 2];
            System.arraycopy(stack, 0, newStack, 0, stackSize);
            stack = newStack;
        }
    }

    private int getLineNumber() {
        return lineNumber + 1;
    }

    private int getLineColumn() {
        return pos - lineStart + 1;
    }
}
