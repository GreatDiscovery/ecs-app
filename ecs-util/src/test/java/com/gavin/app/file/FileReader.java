package com.gavin.app.file;

import com.google.common.io.Files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 工具类，封装了读取文件内容到字符串中的方法
 *
 * @author: Gavin
 * @date: 2021/3/19 17:39
 * @description:
 */
public class FileReader {
    /**
     * 从文件中读取全部内容写到String中
     * @param path
     * @return
     */
    public static String readString(String path) throws IOException {
        File file = new File(path);
        final BufferedReader reader = Files.newReader(file, Charset.defaultCharset());
        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
