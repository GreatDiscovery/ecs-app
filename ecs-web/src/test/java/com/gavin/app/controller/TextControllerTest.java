package com.gavin.app.controller;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author gavin
 * @date 2020/3/7 8:48 下午
 */
public class TextControllerTest {

    @Test
    public void dropSpace() {
        String str = StringUtils.deleteWhitespace("0 1");
        System.out.println(str);
    }
}