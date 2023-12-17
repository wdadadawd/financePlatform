package com.shxt.financePlatform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName course_registration
 */
@TableName(value ="course_registration")
@Data
public class CourseRegistration implements Serializable {
    /**
     * 课程id
     */
    private Integer courseId;

    /**
     * 用户id
     */
    private Integer clientId;

    /**
     * 报名时间
     */
    private Date registrationDate;

    public CourseRegistration() {
    }

    public CourseRegistration(Integer courseId, Integer clientId, Date registrationDate) {
        this.courseId = courseId;
        this.clientId = clientId;
        this.registrationDate = registrationDate;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}