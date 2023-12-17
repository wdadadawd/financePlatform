package com.shxt.financePlatform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName course_appraise
 */
@TableName(value ="course_appraise")
@Data
@NoArgsConstructor
public class CourseAppraise implements Serializable {
    /**
     * 课程id
     */
    private Integer courseId;

    /**
     * 用户id
     */
    private Integer clientId;

    /**
     * 评价等级
     */
    private Double appraiseScore;

    /**
     * 反馈内容
     */
    private String appraiseMessage;

    /**
     * 评价时间
     */
    private Date appraiseTime;

    /**
     * 用户昵称
     */
    @TableField(exist = false)
    private String userName;

    public CourseAppraise(Integer courseId, Integer clientId, Double appraiseScore, String appraiseMessage,Date appraiseTime) {
        this.courseId = courseId;
        this.clientId = clientId;
        this.appraiseScore = appraiseScore;
        this.appraiseMessage = appraiseMessage;
        this.appraiseTime = appraiseTime;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}