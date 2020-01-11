package com.gavin.app.service;

/**
 * 字符串等字面量的处理服务
 *
 * @author gavin
 * @date 2020/1/11 11:12 下午
 */
public interface LiteralService {
    /**
     * 将传进来的字符串格式化成json形式
     *
     * @param unformat
     * @return
     */
    String parseJson(String unformat);
}
