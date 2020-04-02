package com.gavin.app.util;

import java.util.ResourceBundle;

/**
 * 测试配置文件加载类resourceBundle
 *
 * @author gavin
 * @date 2020/4/2 11:03 下午
 */
public class ResourceBundleTest {
    public static void main(String[] args) {
        ResourceBundle bundle = ResourceBundle.getBundle("application");
        System.out.println(bundle.getString("name"));
    }
}
