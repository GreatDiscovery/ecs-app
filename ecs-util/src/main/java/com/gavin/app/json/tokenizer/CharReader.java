package com.gavin.app.json.tokenizer;

import java.io.IOException;
import java.io.Reader;

/**
 * @author gavin
 * @date 2019-12-19 22:50
 */
public class CharReader extends Reader {

    public static final int BUFFER_SIZE = 1024;
    private Reader in;
    private char[] cb;
    private int nextChar;
    private int nChars;

    private int UNMARKED = -1;
    private static final int INVALIDATED = -2;
    private int markedChar = UNMARKED;
    private static int defaultCharBufferSize = 8192;
    private int readAheadLimit = 0; /* Valid only when markedChar > 0 */

    public CharReader(Reader in) {
        this(in, defaultCharBufferSize);
    }

    public CharReader(Reader in, int sz) {
        super(in);
        if (sz <= 0)
            throw new IllegalArgumentException("buffer size <= 0");
        this.in = in;
        this.cb = new char[sz];
    }

    public int read() throws IOException {
        synchronized (lock) {
            ensureOpen();
            for (; ; ) {
                if (nextChar >= nChars) {
                    fill();
                    if (nextChar >= nChars) {
                        return -1;
                    }
                }
                return cb[nextChar++];
            }
        }
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        synchronized (lock) {
            ensureOpen();
            if ((off < 0) || (off > cbuf.length) || (len < 0)
                    || (off + len > cbuf.length) || (off + len < 0)) {
                throw new ArrayIndexOutOfBoundsException();
            } else if (len == 0) {
                return 0;
            }
            int n = read1(cbuf, off, len);
            if (n <= 0) return n;
            while ((n < len) && in.ready()) {
                int n1 = read1(cb, off + n, len - n);
                if (n1 <= 0) break;
                n += n1;
            }
            return n;
        }
    }


    public int read1(char[] cbuf, int off, int len) throws IOException {
        if (nextChar >= nChars) {
            // 如果len太大都接近缓存数组大小了，那么直接把数据读到dst数组中，防止二次搬运
            if (len > cb.length && markedChar <= UNMARKED) {
                return in.read(cbuf, off, len);
            }
            fill();
        }
        // 因为fill需要判断填充情况
        if (nextChar >= nChars) return -1;
        int n = Math.min(len, nChars - nextChar);
        System.arraycopy(cb, nextChar, cbuf, off, n);
        nextChar += n;
        return n;
    }

    public void fill() throws IOException {
        int dst;
        if (markedChar <= UNMARKED) {
            dst = 0;
        } else {
            // todo 未实现mark功能
            int delta = nextChar - markedChar;
            if (delta >= readAheadLimit) {
                markedChar = INVALIDATED;
                readAheadLimit = 0;
                dst = 0;
            } else {
                if (readAheadLimit < cb.length) {
                    dst = delta;
                } else {
                    dst = delta;
                }
            }
        }
        int n;
        do {
            n = in.read(cb, dst, cb.length - dst);
        } while (n == 0);
        if (n > 0) {
            nextChar = dst;
            nChars = dst + n;
        }
    }

    private void ensureOpen() throws IOException {
        if (in == null) {
            throw new IOException("stream close");
        }
    }

    public void mark(int readAheadLimit) throws IOException {
        if (readAheadLimit < 0) {
            throw new IllegalArgumentException("Read-ahead limit < 0");
        }
        synchronized (lock) {
            ensureOpen();
            this.readAheadLimit = readAheadLimit;
            markedChar = nextChar;
        }
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    /**
     * 返回前一个下标的字符
     * @return
     * @throws IOException
     */
    public int peek() throws IOException {
        if (nextChar - 1 >= nChars) {
            return -1;
        }
        return cb[Math.max(0, nextChar - 1)];
    }

    public void back() {
        nextChar = Math.max(0, nextChar--);
    }
 }
