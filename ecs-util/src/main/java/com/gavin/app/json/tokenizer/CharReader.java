package com.gavin.app.json.tokenizer;

import java.io.IOException;
import java.io.Reader;

/**
 * @author gavin
 * @date 2019-12-19 22:50
 */
public class CharReader extends Reader{

    public static final int BUFFER_SIZE = 1024;
    private Reader in;
    private int pos;
    private char[] cb;
    private static int defaultCharBufferSize = 8192;

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

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }
}
