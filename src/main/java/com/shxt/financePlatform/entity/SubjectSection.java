package com.shxt.financePlatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * @TableName subject_section
 */
@TableName(value ="subject_section")
@Data
public class SubjectSection implements Serializable {
    /**
     * 小节id
     */
    @TableId(type = IdType.AUTO)
    private Integer sectionId;

    /**
     * 专题id
     */
    private Integer subjectId;

    /**
     * 小节名称
     */
    private String sectionName;

    /**
     * 回放或视频地址
     */
    private String videoUrl;

    /**
     * 小节类型(直播/视频)
     */
    private String sectionCategory;

    /**
     * 是否已提醒
     */
    private Boolean isRemind;


    /**
     * 开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    private Date startTime;

    public SubjectSection() {
    }

    public SubjectSection(Integer sectionId) {
        this.sectionId = sectionId;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}