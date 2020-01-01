package com.gavin.app.json.internal;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author gavin
 * @date 2020-01-01 20:41
 */
public class LinkedTreeMapTest {
    public static void main(String[] args) {
        LinkedTreeMap<Integer, String> linkedTreeMap = new LinkedTreeMap<>();
        linkedTreeMap.put(1, "1");
        linkedTreeMap.put(3, "3");
        linkedTreeMap.put(2, "2");
        System.out.println(linkedTreeMap.get(2));
    }
}