package com.gavin.app.domain;

import lombok.Data;

/**
 * @author gavin
 * @date 2021/4/16 下午11:16
 */
@Data
public class User {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别
     */
    private String sex;
}

