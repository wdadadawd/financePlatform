package com.shxt.financePlatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName course_subject
 */
@TableName(value ="course_subject")
@Data
public class CourseSubject implements Serializable {
    /**
     * 专题id
     */
    @TableId(type = IdType.AUTO)
    private Integer subjectId;

    /**
     * 课程id
     */
    private Integer courseId;

    /**
     * 主题名称
     */
    private String subjectName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}