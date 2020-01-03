package com.gavin.app.util.pattern;

import java.util.regex.Pattern;

/**
 * 正则测试
 *
 * @author gavin
 * @date 2019-12-22 21:46
 */
public class PatternTest {
    public static void main(String[] args) {
        System.out.println(Pattern.matches("\\d+", "1"));
        System.out.println((int)'1');
    }
}
