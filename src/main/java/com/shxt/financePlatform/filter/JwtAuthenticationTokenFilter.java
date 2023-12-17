package com.shxt.financePlatform.filter;

import com.alibaba.fastjson.JSON;

import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.entity.LoginUser;
import com.shxt.financePlatform.utils.JwtUtil;
import com.shxt.financePlatform.utils.RedisCache;
import com.shxt.financePlatform.utils.WebUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author zt
 * @create 2023-09-18 18:37
 * 检测并解析jwt的拦截器
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisCache redisCache;               //redis工具类

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1.获取token
        String token = request.getHeader("token");
        //2.没有token直接放行(不需要管访问的是什么接口,后续还需要经过spring security的拦截器检测)
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);              //放行
            return;
        }
        //3.有token则解析token,解析之后如果是有效的token,将解析出的LoginUser信息存入springSecurity的上下文中
        LoginUser loginUser;
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);          //解析token
            assert claims != null;
            loginUser = JSON.parseObject(claims.getSubject(),LoginUser.class);                     //获取用户信息
        }catch (ExpiredJwtException var){
            returnError(response,"token已过期,请重新登录");
            return;
        }catch (Exception e) {
            returnError(response,"无效token");
            return;
        }
//      检查当前登录是否有效
        String redisKey = "login:" + claims.getId();
        String cacheToken = redisCache.getCacheObject(redisKey);
        //redis中的token值已经不存在了
        if(Objects.isNull(cacheToken)){
            returnError(response,"token已过期,请重新登录");
            return;
        }
        //token和保存在redis中的token值不同,说明发生了异地登录,避免bug暂时不用
//        if (!cacheToken.equals(token)){
//            returnError(response,"账号在别处登录");
//            return;
//        }
        //存入SecurityContextHolder
        //TODO 获取认证及权限信息封装到Authentication中
//        System.out.println(loginUser);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);                //放行
    }


    //返回错误信息
    public void returnError(HttpServletResponse response,String message){
        R<String> result = R.err(message);
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response,json,401);
    }
}
