package com.shxt.financePlatform.entity;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zt
 * @create 2023-09-17 19:09
 */
@Data
@NoArgsConstructor
//自定义包装用户信息的UserDetails接口实现类
public class LoginUser implements UserDetails {

    //用户信息
    private User user;

    //存储权限信息
    private List<String> permissions;


    //存储SpringSecurity所需要的权限信息的集合
    @JSONField(serialize = false)                  //不转换为json
    private List<GrantedAuthority> authorities;


    public LoginUser(User user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    //获取权限信息
    @Override
    @JSONField(serialize = false)
    public  Collection<? extends GrantedAuthority> getAuthorities() {
        if(authorities!=null){
            return authorities;
        }
        //把permissions中字符串类型的权限信息转换成GrantedAuthority对象存入authorities中
        authorities = permissions.stream().
                map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authorities;
    }

    //获取用户id
    @JSONField(serialize = false)
    public Integer getUserId(){
        return user.getUserId();
    }

    //获取用户身份
    @JSONField(serialize = false)
    public String getIdentity(){
        return user.getIdentity();
    }

    //获取密码
    @Override
    @JSONField(serialize = false)
    public String getPassword() {
        return user.getPassword();
    }

    //获取用户名
    @Override
    @JSONField(serialize = false)
    public String getUsername() {
        return user.getUserAccount();
    }

    //是否没过期
    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonExpired() {
        return true;
    }

    //是否没被锁
    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonLocked() {
        return true;
    }

    //是否没有超时
    @Override
    @JSONField(serialize = false)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //是否可用
    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return true;
    }
}
