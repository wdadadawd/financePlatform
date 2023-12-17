package com.shxt.financePlatform.utils;

import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * @author zt
 * @create 2023-09-10 20:48
 * 用于生成/解析jwt的工具类
 */
public class JwtUtil {
    //有效期为
    public static final Long JWT_TTL = 60 * 60 * 1000 * 7L;    // 60 * 60 *1000  一个小时

    //设置秘钥明文
    public static final String JWT_KEY = "cs";

    /**
     * 获取UUID
     * @return UUID
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    /**
     * 创建token
     * @param id  id
     * @param subject 主题
     * @param ttlMillis 过期时间
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
        return builder.compact();
    }

    //jwt构造器
    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;    //选择签名算法
        SecretKey secretKey = generalKey();                                  //获取加密密钥
        //获取jwt签发时间和过期时间
        Date startDate = new Date(System.currentTimeMillis());     //签发时间
        if(ttlMillis==null){
            ttlMillis=JwtUtil.JWT_TTL;
        }
        Date expDate = new Date(startDate.getTime() + ttlMillis);   //过期时间
        return Jwts.builder()
                .setId(uuid)              //设置jwt的ID
                .setSubject(subject)      //设置jwt的主题  可以是JSON数据
                .setIssuer("zt")          //设置jwt的签发者
                .setIssuedAt(startDate)   //设置jwt的签发时间
                .signWith(signatureAlgorithm, secretKey)    //使用HS256对称加密算法签名,第二个参数为秘钥
                .setExpiration(expDate);  //设置jwt的过期时间
    }


    /**
     * 生成加密后的秘钥 secretKey
     * @return 加密密钥
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }


    /**
     * 解析
     * @param jwt jwt
     * @return 解析出的Claims
     * @throws Exception jwt解析错误异常
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
            return Jwts.parser()
                    .setSigningKey(secretKey)            //设置解析密钥
                    .parseClaimsJws(jwt)                 //设置jwt
                    .getBody();
    }
}