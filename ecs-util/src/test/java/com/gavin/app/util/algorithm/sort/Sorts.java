package com.gavin.app.util.algorithm.sort;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件排序工具
 *
 * @author: Gavin
 * @date: 2021/3/4 12:49
 * @description:
 */
public class Sorts {
    private static final Logger logger = LoggerFactory.getLogger(Sorts.class);

    public static long forkAndJoin(File input, File output, File directory, long memory) throws IOException {
        return forkAndJoin(input, output, directory, memory, true, null, null);
    }

    public static long forkAndJoin(File input, File output, File directory, long memory, Charset inputEncoding, Charset outputEncoding) throws IOException {
        return forkAndJoin(input, output, directory, memory, true, inputEncoding, outputEncoding);
    }

    /**
     * <p>
     * 外部排序, 文件排序, 归并排序
     * </p>
     *
     * @param input     输入文件, 待排序的文件
     * @param output    输出文件, 已排序的文件
     * @param directory 输出目录
     * @param memory    内存大小, 设置用于排序的内存设置, 单位字节. e.g: 1024 * 1024 * 30
     * @param distinct  是否排重
     * @return 输出文件的行数
     * @throws IOException IOException
     */
    public static long forkAndJoin(File input, File output, File directory, long memory, boolean distinct) throws IOException {
        return forkAndJoin(input, output, directory, memory, distinct, null, null);
    }

    /**
     * <p>
     * 外部排序, 文件排序, 归并排序
     * </p>
     *
     * @param input          输入文件, 待排序的文件
     * @param output         输出文件, 已排序的文件
     * @param directory      输出目录
     * @param memory         内存大小, 设置用于排序的内存设置, 单位字节. e.g: 1024 * 1024 * 30
     * @param distinct       是否排重
     * @param inputEncoding  输入文件的编码
     * @param outputEncoding 输出文件的编码
     * @return 输出文件的行数
     * @throws IOException IOException
     */
    public static long forkAndJoin(File input, File output, File directory, long memory, boolean distinct, Charset inputEncoding, Charset outputEncoding) throws IOException {
        List<File> files = null;
        try {
            files = fork(input, directory, memory, distinct, true, inputEncoding, outputEncoding);
            return join(files, output, distinct, inputEncoding, outputEncoding);
        } finally {
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        }
    }

    /**
     * <p>
     * 切分任务并排序, 返回文件列表
     * </p>
     *
     * @param file           输入文件
     * @param directory      临时目录
     * @param memory         可用内存大小-(字节)
     * @param distinct       是否排重
     * @param parallel       是否并行
     * @param inputEncoding  输入文件的编码
     * @param outputEncoding 输出文件的编码
     * @return 排序好的文件
     * @throws IOException IOException
     */
    public static List<File> fork(File file, File directory, long memory, boolean distinct, boolean parallel, Charset inputEncoding, Charset outputEncoding) throws IOException {
        List<File> files = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(file); InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, inputEncoding == null ? defaultEncoding() : inputEncoding); BufferedReader bufferedReader = new BufferedReader(inputStreamReader, DEFAULT_BUFFER_SIZE)) {
            long block = getBLockSize(file.length(), memory);
            String line = "";
            while (line != null) {
                List<String> list = new ArrayList<>();
                long current = 0; // current block size in bytes
                while (current < block && (line = bufferedReader.readLine()) != null) {
                    list.add(line);
                    current += getLength(line);
                }
                files.add(sort(list, directory, distinct, parallel, outputEncoding));
            }
        }
        return files;
    }

    /**
     * <p>
     * 合并已经排序好的文件
     * </p>
     *
     * @param files    排序文件
     * @param output   输出文件
     * @param distinct 是否排重
     * @return 行数
     * @throws IOException IOException
     */
    public static long join(List<File> files, File output, boolean distinct) throws IOException {
        return join(files, output, distinct, null, null);
    }

    /**
     * <p>
     * 合并已经排序好的文件
     * </p>
     *
     * @param files          排序文件
     * @param output         输出文件
     * @param distinct       是否排重
     * @param inputEncoding  输入文件的编码
     * @param outputEncoding 输出文件的编码
     * @return 行数
     * @throws IOException IOException
     */
    public static long join(List<File> files, File output, boolean distinct, Charset inputEncoding, Charset outputEncoding) throws IOException {
        List<BufferedReaderWrapper> wrappers = new ArrayList<>();
        try {
            for (File file : files) {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, inputEncoding == null ? defaultEncoding() : inputEncoding);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                // BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                wrappers.add(new BufferedReaderWrapper(bufferedReader));
            }
            return merge(wrappers, output, distinct, outputEncoding);
        } finally {
            for (BufferedReaderWrapper wrapper : wrappers) {
                wrapper.close();
            }
        }
    }

    /**
     * <p>
     * 内存排序, 最后将结果写入文件
     * </p>
     *
     * @param list           字符集合
     * @param directory      临时目录
     * @param distinct       是否排重
     * @param parallel       是否并行
     * @param outputEncoding 输出文件的编码
     * @return files
     * @throws IOException IOException
     */
    private static File sort(List<String> list, File directory, boolean distinct, boolean parallel, Charset outputEncoding) throws IOException {
        if (parallel) {
            list = list.parallelStream().sorted(DEFAULT_COMPARATOR).collect(Collectors.toList());
        } else {
            list.sort(DEFAULT_COMPARATOR);
        }

        File temp = File.createTempFile("TMP_", ".txt", directory);

        try (FileOutputStream fileOutputStream = new FileOutputStream(temp); OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, outputEncoding == null ? defaultEncoding() : outputEncoding); BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter, DEFAULT_BUFFER_SIZE)) {

            if (distinct) {
                if (!list.isEmpty()) {
                    String last = list.get(0);
                    bufferedWriter.write(last);
                    bufferedWriter.newLine();
                    for (int i = 1; i < list.size(); i++) {
                        String line = list.get(i);
                        if (DEFAULT_COMPARATOR.compare(line, last) != 0) {
                            bufferedWriter.write(line);
                            bufferedWriter.newLine();
                            last = line;
                        }
                    }
                }
            } else {
                for (String line : list) {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }
            }

        }
        return temp;
    }

    /**
     * <p>
     * 内存打撒, 最后将结果写入文件
     * </p>
     *
     * @param list           字符集合
     * @param directory      临时目录
     * @param outputEncoding 输出文件的编码
     * @return files
     * @throws IOException IOException
     */
    private static File shuffile(List<String> list, File directory, Charset outputEncoding) throws IOException{
        Collections.shuffle(list);
        File temp = File.createTempFile("TMP_", ".txt",directory);
        try(FileOutputStream fileOutputStream = new FileOutputStream(temp); OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, outputEncoding == null ? defaultEncoding() : outputEncoding); BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter, DEFAULT_BUFFER_SIZE)){
            for(String line : list){
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        }catch(Exception e){
            logger.error("shuffile 方法无序化操作异常:{}", JSONObject.toJSONString(e));
            throw new IOException("文件无序化操作异常");
        }

        return temp;
    }

    /**
     * <p>
     * 合并已经排序的文件
     * </p>
     *
     * @param wrappers       buffer reader的内部包装类
     * @param output         输出文件
     * @param distinct       是否排重
     * @param outputEncoding 输出文件的编码
     * @return 行数
     * @throws IOException IOException
     */

	/*private static long mergeShuff(List<BufferedReaderWrapper> wrappers, File output, Charset outputEncoding) throws IOException {
		Queue<BufferedReaderWrapper> queue = new LinkedList<>();
		for(BufferedReaderWrapper wrapper : wrappers){
			if (wrapper.exists()){
				queue.offer(wrapper);
			}
		}
		long rows = 0;
		try(FileOutputStream fileOutputStream = new FileOutputStream(output)){

		}catch(Exception e){

		}
		return rows;
	}*/

    private static long merge(List<BufferedReaderWrapper> wrappers, File output, boolean distinct, Charset outputEncoding) throws IOException {
        // 加入到一个优先级队列
        PriorityQueue<BufferedReaderWrapper> queue = new PriorityQueue<>((x, y) -> DEFAULT_COMPARATOR.compare(x.peek(), y.peek()));
        for (BufferedReaderWrapper wrapper : wrappers) {
            if (wrapper.exists()) {
                queue.add(wrapper);
            }
        }

        long rows = 0;
        try (FileOutputStream fileOutputStream = new FileOutputStream(output); OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, outputEncoding == null ? defaultEncoding() : outputEncoding); BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter, DEFAULT_BUFFER_SIZE)) {

            if (distinct) {
                String last = null;

                if (!queue.isEmpty()) {
                    BufferedReaderWrapper wrapper = queue.poll();

                    last = wrapper.poll();
                    bufferedWriter.write(last);
                    bufferedWriter.newLine();
                    rows++;

                    // add it back if exists of close
                    if (wrapper.exists()) {
                        queue.add(wrapper);
                    } else {
                        wrapper.close();
                        queue.remove(wrapper);
                    }
                }

                while (!queue.isEmpty()) {
                    BufferedReaderWrapper wrapper = queue.poll();

                    // skip duplicate lines
                    String line = wrapper.poll();
                    if (DEFAULT_COMPARATOR.compare(line, last) != 0) {
                        bufferedWriter.write(line);
                        bufferedWriter.newLine();
                        last = line;
                        rows++;
                    }

                    // add it back if exists of close
                    if (wrapper.exists()) {
                        queue.add(wrapper);
                    } else {
                        wrapper.close();
                        queue.remove(wrapper);
                    }
                }
            } else {
                while (!queue.isEmpty()) {
                    BufferedReaderWrapper wrapper = queue.poll();
                    bufferedWriter.write(wrapper.poll());
                    bufferedWriter.newLine();
                    rows++;

                    // add it back if exists of close
                    if (wrapper.exists()) {
                        queue.add(wrapper);
                    } else {
                        wrapper.close();
                        queue.remove(wrapper);
                    }
                }
            }

        };
        return rows;
    }

    /**
     * <o>获取一个String对象大概要用多少个字节, 保守策略</o>
     * <p>
     * Data structure alignment
     * </p>
     *
     * @param s string
     * @return 占用字节的数量
     */
    private static long getLength(String s) {
        // int len = OBJECT_LENGTH_64_BIT_JVM + s.getBytes().length;
        // int mod, padding;
        // padding = (mod = (len % 8)) == 0 ? 0 : (8 - mod);
        // return len + padding;
        return s.length() * 2 + OBJECT_LENGTH_64_BIT_JVM;
    }

    /**
     * @param length 文件大小 (字节)
     * @param memory 可用内存大小 (字节)
     * @return 块的大小 (字节)
     */
    private static long getBLockSize(long length, long memory) {
        long block = length / DEFAULT_MAX_TEMP_FILES + (length % DEFAULT_MAX_TEMP_FILES == 0 ? 0 : 1);
        long half = memory / 2;
        return block < half ? half : block;
    }

    /**
     * buffer size
     */
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 1024;

    /**
     * 最大临时文件数量
     */
    private static final long DEFAULT_MAX_TEMP_FILES = 1024;

    /**
     * <p>
     * 64 bit jvm
     * </p>
     * <ul>
     * <li>Object Header : 16 bytes</li>
     * <li>Array Object Header : 24 bytes</li>
     * <li>Object reference : 8 bytes</li>
     * <li>3 int fields : 12 bytes</li>
     * </ul>
     */
    private static final int OBJECT_LENGTH_64_BIT_JVM = 16 + 24 + 8 + 12;

    /**
     * 默认字符串比较器
     */
    private static final Comparator<String> DEFAULT_COMPARATOR = String::compareTo;

    private static class BufferedReaderWrapper implements Closeable {

        private BufferedReader bufferedReader;

        private String value;

        private BufferedReaderWrapper(BufferedReader bufferedReader) throws IOException {
            this.bufferedReader = bufferedReader;
            this.value = this.bufferedReader.readLine();
        }

        @Override
        public void close() {
            try {
                this.bufferedReader.close();
            } catch (IOException ignore) {
            }
        }

        private boolean exists() {
            return this.value != null;
        }

        private String peek() {
            return this.value;
        }

        private String poll() throws IOException {
            String value = this.value;
            this.value = this.bufferedReader.readLine();
            return value;
        }

    }

    private static Charset defaultEncoding() {
        return Charset.defaultCharset();
    }

    public static void main(String[] args){
        Queue<String> queue = new LinkedList<>();
        queue.offer("a");
        queue.offer("b");
        queue.offer("c");
        for (int i = 0; i < 20; i++){
            String str = queue.poll();
            System.out.println("str=" + str);
            queue.offer(str);
        }


        List<Integer> intLst = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            intLst.add(i);
        }

        Collections.shuffle(intLst);
        for (int i = 0; i < intLst.size(); i++){
            if (i % 100 != 0){
                System.out.print(" " + intLst.get(i));
            }else{
                System.out.println(intLst.get(i));
            }

        }
    }

}
