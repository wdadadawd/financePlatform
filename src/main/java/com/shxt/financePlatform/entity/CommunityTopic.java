package com.shxt.financePlatform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName community_topic
 */
@TableName(value ="community_topic")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunityTopic implements Serializable {
    /**
     * 话题id
     */
    @TableId
    private Integer topicId;

    /**
     * 话题标题
     */
    private String topicTitle;

    /**
     * 话题内容
     */
    private String topicContent;

    /**
     * 话题热度
     */
    private Long topicHot;

    /**
     * 点赞数
     */
    private Integer likeSum;

    /**
     * 踩数
     */
    private Integer dislikeSum;

    /**
     * 话题包含的所有的文件链接
     */
    private String fileUrls;

    /**
     * 社区id
     */
    private Integer communityId;

    /**
     * 用户头像链接
     */
    @TableField(exist = false)
    private String profilePhoto;

    /**
     * 用户昵称
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 社区名
     */
    @TableField(exist = false)
    private String communityName;


    /**
     * 发表时间
     */
    private Date sendTime;

    /**
     * 发表用户id
     */
    private Integer userId;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}