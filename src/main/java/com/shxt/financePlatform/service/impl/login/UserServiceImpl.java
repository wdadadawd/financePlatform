package com.shxt.financePlatform.service.impl.login;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shxt.financePlatform.entity.User;
import com.shxt.financePlatform.service.UserService;
import com.shxt.financePlatform.mapper.UserMapper;
import com.shxt.financePlatform.utils.RedisCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
* @author 30567
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-10-20 21:47:36
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisCache redisCache;

    /**
     * 清除用户openId
     * @param userId 用户id
     */
    @Override
    public void cleanOpenId(Integer userId){
        userMapper.updateFieldToNull(userId);
    }

    /**
     * 根据openId查找用户
     * @param openId
     * @return
     */
    @Override
    public User getUserByOpenId(String openId){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("open_id",openId);
        return userMapper.selectOne(queryWrapper);
    }

    /**
     * 根据账号获取用户
     * @param userAccount 账号
     * @return 用户信息
     */
    @Override
    public User getUserByAccount(String userAccount){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        return userMapper.selectOne(queryWrapper);
    }

    /**
     * 检验账号是否重复
     * @param userAccount 账号
     * @return ture(不重复) false(重复)
     */
    @Override
    public boolean checkoutAccountRepetition(String userAccount) {
        User user = getUserByAccount(userAccount);
        //有人正在注册当前这个账号
        Object cacheObject = redisCache.getCacheObject(userAccount);
        return user == null && cacheObject == null;
//        return user == null;
    }

    /**
     * 检验邮箱是否重复
     * @param email 邮箱
     * @return ture(不重复) false(重复)
     */
    @Override
    public boolean checkoutEmailRepetition(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",email);
        User user = userMapper.selectOne(queryWrapper);
        //有人正在注册当前这个邮箱
        Object cacheObject = redisCache.getCacheObject(email);
        return user == null && cacheObject == null;
//        return user == null;
    }

    /**
     * @param userAccount 账号
     * 将账号和邮箱暂存到redis中
     */
    @Override
    public void cacheAccountAndEmail(String userAccount,String email){
        redisCache.setCacheObject(userAccount,"registering",5, TimeUnit.MINUTES);
        redisCache.setCacheObject(email,"registering",5, TimeUnit.MINUTES);
    }

    /**
     * @param userAccount 账号
     * 删除暂存到redis中的账号和邮箱
     */
    @Override
    public void deleteCacheAccountAndEmail(String userAccount,String email){
        redisCache.deleteObject(userAccount);
        redisCache.deleteObject(email);
    }
}




