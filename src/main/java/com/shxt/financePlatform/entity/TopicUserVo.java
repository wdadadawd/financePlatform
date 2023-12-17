package com.shxt.financePlatform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName topic_user_vo
 */
@TableName(value ="topic_user_vo")
@Data
public class TopicUserVo implements Serializable {
    /**
     * 昵称
     */
    private String userName;

    /**
     * 头像
     */
    private String profilePhoto;

    /**
     * 社区名
     */
    private String communityName;

    /**
     * 话题id
     */
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
     * 社区头像
     */
    private String communityPhoto;

    /**
     * 社区id
     */
    private Integer communityId;

    /**
     * 发表用户id
     */
    private Integer userId;

    /**
     * 发表时间
     */
    private Date sendTime;

    /**
     * 点赞数
     */
    private Integer likeSum;

    /**
     * 踩数
     */
    private Integer dislikeSum;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}