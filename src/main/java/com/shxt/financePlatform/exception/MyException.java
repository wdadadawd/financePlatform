package com.shxt.financePlatform.exception;

/**
 * @author zt
 * @create 2023-09-18 18:47
 * 自定义的异常类，用于抛出参数异常导致的运行时异常
 */
public class MyException extends RuntimeException {

    public MyException(String message) {
        super(message);
    }
}
