package com.shxt.financePlatform.handler;

import com.alibaba.fastjson.JSON;
import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zt
 * @create 2023-09-19 11:25
 * springSecurity授权失败处理
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        R<String> result = R.err("权限不足");
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response,json,401);
    }
}
