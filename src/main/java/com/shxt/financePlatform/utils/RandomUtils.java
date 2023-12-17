package com.shxt.financePlatform.utils;

import java.util.Random;

/**
 * @author zt
 * @create 2023-07-13 13:52
 * 随机数工具类
 */
public class RandomUtils {

    //获取length位的随机字符
    public static String getUUID(Integer length){
        // 定义UUID允许的字符
        String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // 创建随机数生成器
        Random random = new Random();

        // 用于存储生成的UUID的StringBuilder
        StringBuilder uuidBuilder = new StringBuilder(length);

        // 通过从允许的字符集合中随机选择字符来生成UUID
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allowedCharacters.length());
            char randomChar = allowedCharacters.charAt(randomIndex);
            uuidBuilder.append(randomChar);
        }

        // 将生成的UUID作为字符串返回
        return uuidBuilder.toString();
    }
}
