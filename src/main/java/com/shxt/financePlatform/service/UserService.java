package com.shxt.financePlatform.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shxt.financePlatform.entity.User;

/**
* @author 30567
* @description 针对表【user】的数据库操作Service
* @createDate 2023-10-20 21:47:36
*/
public interface UserService extends IService<User> {


    void cleanOpenId(Integer userId);

    User getUserByOpenId(String openId);

    User getUserByAccount(String userAccount);

    boolean checkoutAccountRepetition(String userAccount);

    boolean checkoutEmailRepetition(String email);

    void cacheAccountAndEmail(String userAccount,String email);

    void deleteCacheAccountAndEmail(String userAccount,String email);
}
