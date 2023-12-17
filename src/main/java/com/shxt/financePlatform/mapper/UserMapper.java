package com.shxt.financePlatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shxt.financePlatform.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* @author 30567
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-10-20 21:47:36
* @Entity generator.entity.User
*/
public interface UserMapper extends BaseMapper<User> {


    /**
     * 清空用户的openId
     * @param userId 用户id
     */
    @Update("UPDATE user SET open_id = NULL WHERE user_id = #{userId}")
    void updateFieldToNull(@Param("userId") Integer userId);

    /**
     * 获取用户角色信息
     * @param userId 用户id
     * @return
     */
    @Select("SELECT identity \n" +
            "FROM `user`\n" +
            "WHERE user_id = #{userId}")
    List<String> getUserRole(@Param("userId") Integer userId);


    /**
     * 获取用户权限信息
     * @param userId 用户id
     * @return
     */
    @Select("SELECT permission_name \n" +
            "FROM `user` LEFT JOIN role_permission\n" +
            "ON `user`.identity = role_permission.role_name\n" +
            "WHERE user_id = #{userId}")
    List<String> getUserPermission(@Param("userId") Integer userId);
}




