package com.gavin.app.json.model;

import org.junit.Assert;

/**
 * @author gavin
 * @date 2019-12-28 21:48
 */
public class JsonPrimitiveTest {
    public static void main(String[] args) {
        Assert.assertTrue(A.class.isAssignableFrom(B.class));
    }

    class A {
    }

    class B extends A {
    }
}
