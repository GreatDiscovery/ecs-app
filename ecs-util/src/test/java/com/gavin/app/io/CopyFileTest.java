package com.gavin.app.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

/**
 * 测试零拷贝和普通拷贝之间性能的差别
 *
 * @author: Gavin
 * @date: 2020/3/13 14:08
 * @description:
 */
public class CopyFileTest {
    public static void main(String[] args) throws Exception {
//        Scanner scanner = new Scanner(System.in);
////        String path = scanner.next();
////        System.out.println(path);
        String path = "D:\\下载\\openjdk-8u41-src-b04-14_jan_2020.zip";
//        ioCopy(path);
        nioCopyTest1(path);
//        nioCopyTest2(path);
//        nioCopy3(path);
    }

    private static void nioCopy3(String path) throws IOException {
        long startTime = System.currentTimeMillis();

        FileChannel inChannel = FileChannel.open(Paths.get(path), StandardOpenOption.READ);
        FileChannel outChennel = FileChannel.open(Paths.get("/tmp/nio3"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE_NEW);
        outChennel.transferFrom(inChannel, 0, inChannel.size());

        long end = System.currentTimeMillis();
        System.out.println("nioCopy3耗费时间:" + (end - startTime));
    }

    private static void ioCopy(String path) {
        long startTime = System.currentTimeMillis();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(path));
            outputStream = new BufferedOutputStream(new FileOutputStream("/tmp/io"));
            byte[] bytes = new byte[1024];
            int i;
            //读取到输入流数据，然后写入到输出流中去，实现复制
            while ((i = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("ioCopy耗费时间:" + (end - startTime));
    }

    private static void  nioCopyTest2(String path) throws IOException {
        long startTime = System.currentTimeMillis();

        FileChannel inChannel = FileChannel.open(Paths.get(path), StandardOpenOption.READ);

        FileChannel outChennel = FileChannel.open(Paths.get("/tmp/nio2"),StandardOpenOption.WRITE,StandardOpenOption.READ,StandardOpenOption.CREATE_NEW);

        //内存映射文件(什么模式 从哪开始 到哪结束)
        MappedByteBuffer inMappeBuf = inChannel.map(FileChannel.MapMode.READ_ONLY,0,inChannel.size());
        MappedByteBuffer outMappeBuf =  outChennel.map(FileChannel.MapMode.READ_WRITE,0,inChannel.size());

        //直接都缓冲区进行数据的读写操作
        byte[] dst = new byte[inMappeBuf.limit()];
        inMappeBuf.get(dst);
        outMappeBuf.put(dst);

        inChannel.close();
        outChennel.close();
        long end = System.currentTimeMillis();
        System.out.println("nioCopyTest2耗费时间:"+(end-startTime));
    }

    /**
     *  非直接缓冲区 文件的复制
     * @throws IOException
     */
    private static void nioCopyTest1(String path)throws IOException {
        long startTime = System.currentTimeMillis();
        FileInputStream fileInputStream = new FileInputStream(path);
        FileOutputStream fileOutputStream = new FileOutputStream("/tmp/nio1");

        //获取通道
        FileChannel inChannel = fileInputStream.getChannel();
        FileChannel outChanel = fileOutputStream.getChannel();

        //分配缓冲区的大小
        ByteBuffer buf = ByteBuffer.allocate(1024);

        //将通道中的数据存入缓冲区
        while (inChannel.read(buf) != -1) {
            buf.flip();//切换读取数据的模式
            outChanel.write(buf);
            buf.clear();
        }
        outChanel.close();
        inChannel.close();
        fileInputStream.close();
        fileOutputStream.close();
        long end = System.currentTimeMillis();
        System.out.println("nioCopyTest1耗费时间:"+(end-startTime));
    }
}
