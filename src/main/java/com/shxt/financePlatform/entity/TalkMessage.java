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
 * @TableName talk_message
 */
@TableName(value ="talk_message")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TalkMessage implements Serializable {

    /**
     * 消息id
     */
    @TableId
    private String messageId;

    /**
     * 消息所属用户id
     */
    private Integer userId;

    /**
     *  消息所对应的角色
     */
    private String role;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息产生的时间
     */
    private Date time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}