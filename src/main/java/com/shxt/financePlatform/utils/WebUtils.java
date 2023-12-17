package com.shxt.financePlatform.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zt
 * @create 2023-09-10 20:52
 */
public class WebUtils
{
    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string 待渲染的字符串
     * @return null
     */
    public static String renderString(HttpServletResponse response, String string,int code) {
        try
        {
            response.setStatus(code);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}