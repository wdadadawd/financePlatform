package com.shxt.financePlatform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

/**
 * 
 * @TableName topic_comment
 */
@TableName(value ="topic_comment")
@Data
public class TopicComment implements Serializable {
    /**
     * 评论id
     */
    @TableId
    private Long commentId;

    /**
     * 话题id
     */
    private Integer topicId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 点赞数
     */
    private Integer likeSum;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 踩数
     */
    private Integer dislikeSum;

    /**
     * 被回复的评论id
     */
    private Long repliedId;

    /**
     * 回复包含的所有的文件链接
     */
    private String fileUrls;

    /**
     * 发表时间
     */
    private Date sendTime;

    /**
     * 最顶部评论的id
     */
    private Long fatherId;

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
     * 用户回复人名称
     */
    @TableField(exist = false)
    private String repliedName;

    /**
     * 评论的回复信息
     */
    @TableField(exist = false)
    private IPage<TopicComment> replyPage;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}