package com.gavin.app.common.exception;

/**
 * @author: Gavin
 * @date: 2021/7/1 15:07
 * @description:
 */
public class NoSuchPropertyException extends RuntimeException {
    public NoSuchPropertyException(String message) {
        super(message);
    }
}
