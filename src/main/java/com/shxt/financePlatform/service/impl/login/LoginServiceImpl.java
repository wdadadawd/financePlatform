package com.shxt.financePlatform.service.impl.login;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.shxt.financePlatform.entity.LoginUser;
import com.shxt.financePlatform.entity.User;
import com.shxt.financePlatform.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * @author zt
 * @create 2023-09-17 17:15
 */
//1.自定义springSecurity的认证操作
//(1).实现UserDetailsService接口
//(2).自定义LoginUser类实现UserDetails接口以及其中的方法
//(3).重写loadUserByUsername方法
@Service
public class LoginServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    //springSecurity默认调用的认证方法
    @Override
    public UserDetails loadUserByUsername(String userAccount)  {
        //1.查询对应用户名对应的用户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        User user = userMapper.selectOne(queryWrapper);
        //2.获取权限信息
        List<String> permissionList = userMapper.getUserPermission(user.getUserId()); //获取用户权限
        List<String> roleList = userMapper.getUserRole(user.getUserId());   //获取用户角色
        List<String> mergedList = new ArrayList<>();
        for (String r: roleList) {
            if (r != null)
                mergedList.add("ROLE_" + r);
        }
        for (String p: permissionList){
            if (p != null)
                mergedList.add(p);
        }
        //4.返回包装用户信息的UserDetails实现类
        return new LoginUser(user,mergedList);
    }
}
