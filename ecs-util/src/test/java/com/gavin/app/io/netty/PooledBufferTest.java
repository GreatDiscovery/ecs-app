package com.gavin.app.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.apache.commons.lang.StringUtils;

import java.nio.charset.Charset;

/**
 * @author gavin
 * @date 2020/6/21 10:00 下午
 */
public class PooledBufferTest {
    public static void main(String[] args) {
        PooledByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;
        ByteBuf byteBuf = allocator.directBuffer(16);
        String str = "hello";
        byteBuf.setBytes(0, str.getBytes());
        System.out.println(byteBuf.getCharSequence(0, 5, Charset.defaultCharset()));
        byteBuf.release();
        System.out.println(PooledBufferTest.class.getClassLoader());
    }
}
