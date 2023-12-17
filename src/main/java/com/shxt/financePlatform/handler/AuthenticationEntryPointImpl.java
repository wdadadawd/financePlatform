package com.shxt.financePlatform.handler;

import com.alibaba.fastjson.JSON;
import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.utils.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zt
 * @create 2023-09-19 11:16
 * springSecurity认证失败处理
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        R<String> result = R.err("认证失败,请重新登录");
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response,json,401);
    }
}
