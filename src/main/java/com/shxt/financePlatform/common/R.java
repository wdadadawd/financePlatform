package com.shxt.financePlatform.common;

import lombok.Data;

/**
 * @author zt
 * @create 2023-02-23 22:00
 */
@Data
public class R<T> {
    private String message;        //错误消息提示
    private int code;              //为1则操作成功，为0则操作失败
    private T data;                //成功时需要返回的数据

    //私有化构造器
    private R(){}

    public static <T> R<T> success(T object,String message) {
        R<T> tr = new R<>();
        tr.code = 1;
        tr.data = object;
        tr.message = message;
        return tr;
    }

    public static <T> R<T> err(String message) {
        R<T> tr = new R<>();
        tr.code = 0;
        tr.message = message;
        return tr;
    }
}
