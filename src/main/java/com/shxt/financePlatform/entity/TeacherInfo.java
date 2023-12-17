package com.shxt.financePlatform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName teacher_info
 */
@TableName(value ="teacher_info")
@Data
public class TeacherInfo implements Serializable {
    /**
     * 用户id
     */
    @TableId
    private Integer userId;

    /**
     * 昵称
     */
    private String userName;

    /**
     * 教龄
     */
    private Integer teachingAge;

    /**
     * 头像链接
     */
    private String profilePhoto;

    /**
     * 教学风格
     */
    private String teachingStyle;

    /**
     * 教学资历(获取的证书)
     */
    private String teachingQualification;

    /**
     * 认证状态
     */
    private String authenticationState;

    /**
     * 教师简介
     */
    private String teacherProfile;


    public TeacherInfo(Integer userId, String authenticationState,String userName,String profilePhoto) {
        this.userId = userId;
        this.authenticationState = authenticationState;
        this.userName = userName;
        this.profilePhoto = profilePhoto;
    }

    public TeacherInfo() {
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}