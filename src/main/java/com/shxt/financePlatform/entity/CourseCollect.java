package com.shxt.financePlatform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName course_collect
 */
@TableName(value ="course_collect")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseCollect implements Serializable {
    /**
     * 课程id
     */
    private Integer courseId;

    /**
     * 用户id
     */
    private Integer clientId;

    /**
     * 收藏时间
     */
    private Date collectTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}