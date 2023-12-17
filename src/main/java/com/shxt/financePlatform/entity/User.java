package com.shxt.financePlatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Integer userId;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String userName;

    /**
     * 头像
     */
    private String profilePhoto;

    /**
     * 身份(admin,teacher,client)
     */
    private String identity;

    /**
     * 微信公众号openId
     */
    private String openId;

    public User() {
    }

    public User(Integer userId, String userName, String profilePhoto) {
        this.userId = userId;
        this.userName = userName;
        this.profilePhoto = profilePhoto;
    }

    public User(Integer userId, String openId) {
        this.userId = userId;
        this.openId = openId;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}