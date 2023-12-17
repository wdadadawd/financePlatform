package com.shxt.financePlatform.config;

import com.shxt.financePlatform.filter.JwtAuthenticationTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @author zt
 * @create 2023-09-17 19:50
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{   //extends WebSecurityConfigurerAdapter

    @Resource
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Resource
    private AccessDeniedHandler accessDeniedHandler;

    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    //配置加密,new BCryptPasswordEncoder().encode("密码")
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //认证管理器
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()       //关闭csrf
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //不通过Session获取SecurityContext
             .and().authorizeRequests()   //配置权限控制
                // 放行接口anonymous只支持匿名访问(未登录)
                .antMatchers("/login","/register","/sendRegisterCode","/verifyRegister").anonymous()
                //permitAll允许所有状况访问
                .antMatchers("/getAllCourse","/courseInfo","/courseAllSubject","/subjectAllSection","/liveInfo",
                        "/courseFirstFile","/courseNFile","/courseAppraise","/topicComment","/commentReply",
                        "/studyCommunity","/recommendCommunity","/recommendTopic","/communityTopic",
                        "/binding","/unsubscribe","/topicInfo").permitAll()
                .anyRequest().authenticated()   // 除上面外的所有请求全部需要鉴权认证
            .and().formLogin()          //配置登录
                .loginPage("/login.html")
                .permitAll();
        //把token校验过滤器添加到过滤器链中,添加在过滤器的最前面
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //配置处理授权和认证异常的事务
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
        //允许跨域
        http.cors();
    }
}
