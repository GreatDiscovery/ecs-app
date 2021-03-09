package com.gavin.app.file;

import com.google.common.io.Files;

import java.io.*;
import java.nio.charset.Charset;

/**
 * 文件工具类测试
 *
 * @author: Gavin
 * @date: 2021/3/9 16:43
 * @description:
 */
public class FileUtilTest {
    public static void main(String[] args) throws IOException {
        File file = new File(String.format("hello-%s.txt", System.currentTimeMillis()));
        BufferedWriter writer = Files.newWriter(file, Charset.defaultCharset());
        BufferedReader reader = Files.newReader(file, Charset.defaultCharset());
        Files.append("world", file, Charset.defaultCharset());
    }
}
