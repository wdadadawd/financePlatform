package com.shxt.financePlatform.service;

import java.io.InputStream;

/**
 * @author zt
 * @create 2023-09-22 22:32
 */
public interface FileService {


    /**
     * 文件上传阿里云
     * @param inputStream 文件输入流
     * @param module 文件存储的文件夹名
     * @param originalFilename 原文件名
     * @return
     */
    String upload(InputStream inputStream, String module, String originalFilename);

    /**
     * 删除文件
     * @param url 文件地址
     */
    void remove(String url);
}

