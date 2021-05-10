package com.gavin.app.lambda;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

/**
 * @author: Gavin
 * @date: 2021/5/10 14:53
 * @description:
 */
public class ThrowingConsumerTest {
    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(3, 9, 7, 0, 10, 20);
        integers.forEach(ThrowingConsumer.handlingConsumerWrapper(
                i -> writeToFile(i), IOException.class));
    }

    public static void writeToFile(int i) throws IOException {
        Files.newBufferedWriter(null, null);
    }
}
