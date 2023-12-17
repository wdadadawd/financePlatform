package com.shxt.financePlatform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName client_info
 */
@TableName(value ="client_info")
@Data
public class ClientInfo implements Serializable {
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
     * 头像链接
     */
    private String profilePhoto;

    /**
     * 生日
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birthday;

    /**
     * 性别
     */
    private String sex;

    /**
     * 是否开启个性化(默认开启)
     */
    private Boolean individuation;

    /**
     * 职位
     */
    private String position;

    /**
     * 受教育程度
     */
    private String education;

    /**
     * 学习风格喜好
     */
    private String preferredStyle;

    /**
     * 每日空闲时间[xx:xx-xx:xx,xx:xx-xx]
     */
    private String freeTime;

    /**
     * 每日期望学习时长
     */
    private Integer studyTime;

    /**
     * 目标职位
     */
    private String positionTarget;

    /**
     * 学习科目对应课程类别
     */
    private String studyCategory;

    public ClientInfo(Integer userId, Boolean individuation,String userName,String profilePhoto) {
        this.userId = userId;
        this.individuation = individuation;
        this.userName = userName;
        this.profilePhoto = profilePhoto;
    }

    public ClientInfo() {
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}