package com.gavin.app.base;

import org.junit.Test;

/**
 * String 类型的一些测试
 *  @author gavin
 * @date 2021/1/23 下午8:42
 */
public class StringTest {
    @Test
    public void equalTest() {
        String s1 = new String("1");
        String s2 = new String("1");
        System.out.println("s1 == s2 = " + (s1 == s2));
        System.out.println("s1.equals(s2) = " + s1.equals(s2));
        System.out.println("System.identityHashCode(s1) = " + System.identityHashCode(s1));
        System.out.println("System.identityHashCode(s2) = " + System.identityHashCode(s2));

        String s3 = "1";
        String s4 = "1";
        System.out.println("s3 == s4 = " + (s3 == s4));
        System.out.println("s3.equals(s4) = " + s3.equals(s4));
        System.out.println("System.identityHashCode(s3) = " + System.identityHashCode(s3));
        System.out.println("System.identityHashCode(s4) = " + System.identityHashCode(s4));

        System.out.println("s1 == s3 = " + (s1 == s3));
        System.out.println("s1.equals(s3) = " + s1.equals(s3));

        String s5 = "kai gao da!";
        String s6 = "kai gao da!";
        System.out.println("s5 == s6 = " + (s5 == s6));
        System.out.println("System.identityHashCode(s5) = " + System.identityHashCode(s5));
        System.out.println("System.identityHashCode(s6) = " + System.identityHashCode(s6));

        /**-----------------------------真神奇--------------------*/
        String s7 = new String("kai fei ji");
        String s77 = s7.intern();
        String s8 = "kai fei ji";
        System.out.println("(s7 == s8) = " + (s7 == s8));
        System.out.println("(s77 == s8) = " + (s77 == s8));
        System.out.println("System.identityHashCode(s7) = " + System.identityHashCode(s7));
        System.out.println("System.identityHashCode(s8) = " + System.identityHashCode(s8));

        String s9 = new String("abc!") + new String("efg");
        String s99 = s9.intern();
        String s10 = "abc!efg";
        System.out.println("(s9 == s10) = " + (s9 == s10));
        System.out.println("(s99 == s10) = " + (s99 == s10));
        System.out.println("System.identityHashCode(s9) = " + System.identityHashCode(s9));
        System.out.println("System.identityHashCode(s10) = " + System.identityHashCode(s10));
    }

    @Test
    public void test2() {
        String str1 = "string";
        String str2 = new String("string");
        String str3 = str2.intern();

        System.out.println(str1==str2);
        System.out.println(str1==str3);
    }
}
