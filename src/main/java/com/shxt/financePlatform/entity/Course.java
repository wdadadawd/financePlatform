package com.shxt.financePlatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 
 * @TableName course
 */
@TableName(value ="course")
@Data
public class Course implements Serializable {
    /**
     * 课程id
     */
    @TableId(type = IdType.AUTO)
    private Integer courseId;

    /**
     * 教师id
     */
    private Integer teacherId;

    /**
     * 课程科目
     */
    private String literaryCourse;

    /**
     * 课程封面链接
     */
    private String courseCover;

    /**
     * 旧价格
     */
    private Double oldPrice;

    /**
     * 新价格
     */
    private Double newPrice;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 文字描述
     */
    private String textDescription;

    /**
     * 课程图片描述链接
     */
    private String pictureDescription;

    /**
     * 课程类别(直播/视频/视频+直播)
     */
    private String courseCategory;

    /**
     * 课程是否发布
     */
    private Boolean Published;

    /**
     * 发布时间
     */
    private Date publishedDate;

    /**
     * 课时
     */
    @TableField(exist = false)
    private Integer classHour;

    public Course(){

    }

    public Course(Integer courseId) {
        this.courseId = courseId;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}