package com.shxt.financePlatform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName user_ticket
 */
@TableName(value ="user_ticket")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTicket implements Serializable {
    /**
     * 用户id
     */
    @TableId
    private Integer userId;

    /**
     * ticket凭证
     */
    private String ticket;

    /**
     * 过期的时间戳(秒)
     */
    private Long expireTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}