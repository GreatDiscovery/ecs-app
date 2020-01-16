package com.gavin.app.json.internal;

import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * 格式正确，内容错误的单元测试
 *
 * @author gavin
 * @date 2020/1/15 10:57 下午
 */
public class JsonWriterTest {

    Class<?> clazz;
    JsonWriter jsonWriter;

    @Before
    public void before() {
        jsonWriter = new JsonWriter(new StringWriter());
        clazz = jsonWriter.getClass();
    }

    @Test
    public void string() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method stringMethod = clazz.getDeclaredMethod("string", String.class);
        stringMethod.setAccessible(true);
        stringMethod.invoke(jsonWriter, "hello\n");
    }

    public static void main(String[] args) {
        System.out.println("100");
    }
}