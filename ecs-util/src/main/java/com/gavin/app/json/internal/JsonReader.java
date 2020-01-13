package com.gavin.app.json.internal;

import com.gavin.app.json.exception.JsonParseException;

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

    /**
     * A peeked value that was composed entirely of digits with an optional
     * leading dash. Positive values may not have a leading 0.
     */
    private long peekedLong;

    /**
     * The number of characters in a peeked number literal. Increment 'pos' by
     * this after reading a number.
     */
    private int peekedNumberLength;


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
            case PEEKED_SINGLE_QUOTED:
            case PEEKED_UNQUOTED:
            case PEEKED_DOUBLE_QUOTED:
                return JsonToken.STRING;
            case PEEKED_BEGIN_ARRAY:
                return JsonToken.BEGIN_ARRAY;
            case PEEKED_LONG:
            case PEEKED_NUMBER:
                return JsonToken.NUMBER;

        }
        return null;
    }

    private int doPeek() throws IOException {
        int peekStack = stack[stackSize - 1];
        if (peekStack == JsonScope.EMPTY_DOCUMENT) {
            stack[stackSize - 1] = JsonScope.NONEMPTY_DOCUMENT;
        } else if (peekStack == JsonScope.EMPTY_OBJECT || peekStack == JsonScope.NONEMPTY_OBJECT) {
            stack[stackSize - 1] = JsonScope.DANGLING_NAME;
            if (peekStack == JsonScope.NONEMPTY_OBJECT) {
                int c = nextNonWhitespace(true);
                switch (c) {
                    case '}':
                        return peeked = PEEKED_END_OBJECT;
                    case ',':
                        break;
                    default:
                        throw new IOException("Unterminated object");
                }

            }

            int c = nextNonWhitespace(true);
            switch (c) {
                case '"':
                    return peeked = PEEKED_DOUBLE_QUOTED_NAME;
                case '\'':
                    return peeked = PEEKED_SINGLE_QUOTED_NAME;
                default:
                    pos--;
                    return peeked = PEEKED_UNQUOTED_NAME;

            }
        } else if (peekStack == JsonScope.DANGLING_NAME) {
            stack[stackSize - 1] = JsonScope.NONEMPTY_OBJECT;
            int c = nextNonWhitespace(true);
            switch (c) {
                case ':':
                    break;
            }

        } else if (peekStack == JsonScope.EMPTY_ARRAY) {
            stack[stackSize - 1] = JsonScope.NONEMPTY_ARRAY;
        } else if (peekStack == JsonScope.NONEMPTY_ARRAY) {

        }

        int c = nextNonWhitespace(true);
        switch (c) {
            case '{':
                return peeked = PEEKED_BEGIN_OBJECT;
            case '}':
                return peeked = PEEKED_END_OBJECT;
            case '[':
                return peeked = PEEKED_BEGIN_ARRAY;
            case ']':
                return peeked = PEEKED_END_ARRAY;
            case '"':
                return peeked = PEEKED_DOUBLE_QUOTED;
            default:
                // 说明读取不到特殊符号，读到的是一个字面量
                pos--;
        }

        int result = peekKeyword();
        if (result != PEEKED_NONE) {
            return result;
        }

        result = peekNumber();
        if (result != PEEKED_NONE) {
            return result;
        }

        if (!isLiteral(buffer[pos])) {
            throw new JsonParseException("Expected value");
        }

        return peeked = PEEKED_UNQUOTED;
    }

    // 判断true、false、null
    private int peekKeyword() throws IOException {
        char c = buffer[pos];
        String keyword;
        String keywordUpper;
        int peeking;

        if (c == 't' || c == 'T') {
            keyword = "true";
            keywordUpper = "TRUE";
            peeking = PEEKED_TRUE;
        } else if (c == 'f' || c == 'F') {
            keyword = "false";
            keywordUpper = "FALSE";
            peeking = PEEKED_FALSE;
        } else if (c == 'n' || c == 'N') {
            keyword = "null";
            keywordUpper = "NULL";
            peeking = PEEKED_NULL;
        } else {
            return PEEKED_NONE;
        }

        // 验证是否是期望的值，继续往下比较
        int length = keyword.length();
        for (int i = 1; i < length; i++) {
            if (pos + i >= limit && !fill(i + 1)) {
                return PEEKED_NONE;
            }
            c = buffer[i];
            if (c != keyword.charAt(i) && c != keywordUpper.charAt(i)) {
                return PEEKED_NONE;
            }
        }

        // 严格规定格式true等，不允许出现trues等多余的字面量
        if ((pos + length < limit || fill(length + 1)) &&
                isLiteral(c)) {
            return PEEKED_NONE;
        }

        pos += length;
        return peeked = peeking;
    }

    private boolean isLiteral(char c) {
        switch (c) {
            case '{':
            case '}':
            case '[':
            case ']':
            case '=':
            case '\\':
            case ' ':
            case ':':
            case ',':
            case '\t':
            case '\n':
            case '\r':
            case '\f':
            case '/':
            case '#':
                return false;
            default:
                return true;
        }
    }

    // 处理多种数字类型，比如double,0.1,2*e1等
    private int peekNumber() throws IOException {
        char[] buffer = this.buffer;
        int p = pos;
        int l = limit;

        long value = 0;
        boolean negative = false;
        // 用这个值来确保可以被转换成long类型
        boolean fitsInLong = true;
        int last = NUMBER_CHAR_NONE;

        int i = 0;

        charactersOfNumber:
        for (; true; i++) {
            if (p + i == l) {
                // 如果太长了，就不转了，交给后面字面量去处理
                if (i == buffer.length) {
                    return PEEKED_NONE;
                }
                if (!fill(i + 1)) {
                    break;
                }

                p = pos;
                l = limit;
            }

            char c = buffer[p + i];
            switch (c) {
                case '-':
                    if (last == NUMBER_CHAR_NONE) {
                        negative = true;
                        last = NUMBER_CHAR_SIGN;
                        continue;
                    } else if (last == NUMBER_CHAR_EXP_E) {
                        last = NUMBER_CHAR_EXP_SIGN;
                        continue;
                    }
                    return PEEKED_NONE;
                case '+':
                    if (last == NUMBER_CHAR_EXP_SIGN) {
                        last = NUMBER_CHAR_EXP_SIGN;
                        continue;
                    }
                    return PEEKED_NONE;
                case 'e':
                case 'E':
                    if (last == NUMBER_CHAR_DIGIT || last == NUMBER_CHAR_FRACTION_DIGIT) {
                        last = NUMBER_CHAR_EXP_E;
                        continue;
                    }
                    return PEEKED_NONE;
                case '.':
                    if (last == NUMBER_CHAR_DIGIT) {
                        last = NUMBER_CHAR_DECIMAL;
                        continue;
                    }
                    return PEEKED_NONE;

                default:
                    if (c < '0' || c > '9') {
                        if (!isLiteral(c)) {
                            break charactersOfNumber;
                        }
                        return PEEKED_NONE;
                    }
                    if (last == NUMBER_CHAR_SIGN || last == NUMBER_CHAR_NONE) {
                        value = -(c - '0');
                        last = NUMBER_CHAR_DIGIT;
                    } else if (last == NUMBER_CHAR_DIGIT) {
                        // 不允许出现001这种数
                        if (c == '0') {
                            return PEEKED_NONE;
                        }
                        long newValue = value * 10 - (c - '0');
                        fitsInLong &= value > MIN_INCOMPLETE_INTEGER || (value == MIN_INCOMPLETE_INTEGER && newValue < value);
                    } else if (last == NUMBER_CHAR_DECIMAL) {
                        last = NUMBER_CHAR_FRACTION_DIGIT;
                    } else if (last == NUMBER_CHAR_EXP_E || last == NUMBER_CHAR_EXP_SIGN) {
                        last = NUMBER_CHAR_EXP_DIGIT;
                    }
            }

        }

        if (last == NUMBER_CHAR_DIGIT && fitsInLong && (value != 0 || false == negative)) {
            peekedLong = negative ? value : -value;
            pos += i;
            return peeked = PEEKED_LONG;
        } // 这里遇到小数和E就不进行转换，直接按照字面量进行提取
        else if (last == NUMBER_CHAR_DIGIT || last == NUMBER_CHAR_FRACTION_DIGIT
                || last == NUMBER_CHAR_EXP_DIGIT) {
            peekedNumberLength = i;
            return peeked = PEEKED_NUMBER;
        } else {
            return PEEKED_NONE;
        }
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

    public String nextString() throws IOException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }

        String result = "";
        if (p == PEEKED_DOUBLE_QUOTED) {
            result = nextQuotedValue('"');
        } else if (p == PEEKED_SINGLE_QUOTED) {
            result = nextQuotedValue('\'');
        } else {
            throw new IllegalStateException("Expected a string but was " + peek() + locationString());
        }

        peeked = PEEKED_NONE;
        return result;
    }

    public Boolean nextBoolean() throws IOException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }

        if (p == PEEKED_TRUE ) {
            peeked = PEEKED_NONE;
            return true;
        } else if (p == PEEKED_FALSE) {
            peeked = PEEKED_NONE;
            return false;
        }
        throw new IllegalStateException("Expected a boolean but was a " + peek() + locationString());
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
        return " at line " + line + "at column " + column;
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

    public void beginArray() throws IOException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            doPeek();
        }

        if (p == PEEKED_BEGIN_ARRAY) {
            push(JsonScope.EMPTY_ARRAY);
            peeked = PEEKED_NONE;
        } else {
            throw new IllegalStateException("Expected BEGIN_ARRAY but was " + peek() +
                    " at line " + getLineNumber() + " at column " + getLineColumn());
        }
    }

    public void endArray() throws IOException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            doPeek();
        }

        if (p == PEEKED_END_ARRAY) {
            stackSize--;
            peeked = PEEKED_NONE;
        } else {
            throw new IllegalStateException("Expected END_ARRAY but was " + peek()
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
        if (p == PEEKED_DOUBLE_QUOTED_NAME) {
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
