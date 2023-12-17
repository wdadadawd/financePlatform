package com.shxt.financePlatform.utils;

import com.shxt.financePlatform.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author zt
 * @create 2023-10-22 20:36
 */
public class AuthenticationUtils {


    //解析获取用户id
    public static Integer getUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return loginUser.getUserId();
    }


    //解析获取用户身份
    public static String getIdentity(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return loginUser.getIdentity();
    }
}
