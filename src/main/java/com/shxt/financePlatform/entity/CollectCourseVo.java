package com.shxt.financePlatform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName collect_course_vo
 */
@TableName(value ="collect_course_vo")
@Data
public class CollectCourseVo implements Serializable {
    /**
     * 用户id
     */
    private Integer clientId;

    /**
     * 课程id
     */
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
     * 昵称
     */
    private String teacherName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}