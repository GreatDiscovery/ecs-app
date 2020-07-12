package com.gavin.app.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.apache.commons.lang.StringUtils;

/**
 * @author gavin
 * @date 2020/6/21 10:00 下午
 */
public class PooledBufferTest {
    public static void main(String[] args) {
        PooledByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;
        ByteBuf byteBuf = allocator.directBuffer(16);
        byteBuf.release();
        System.out.println(PooledBufferTest.class.getClassLoader());
    }
}
