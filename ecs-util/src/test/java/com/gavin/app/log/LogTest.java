package com.gavin.app.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 日志系统源码初探
 * @author gavin
 * @date 2021/5/2 下午4:12
 */
public class LogTest {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(LogTest.class);
        logger.info("hello world");
    }
}
