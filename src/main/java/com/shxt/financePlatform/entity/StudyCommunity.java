package com.shxt.financePlatform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName study_community
 */
@TableName(value ="study_community")
@Data
@NoArgsConstructor
public class StudyCommunity implements Serializable {
    /**
     * 社区id
     */
    @TableId
    private Integer communityId;

    /**
     * 创建者id
     */
    private Integer createId;

    /**
     * 社区类型 open(开放) course(课程)
     */
    private String communityType;

    /**
     * 社区简介
     */
    private String communityIntroduce;

    /**
     * 社区名
     */
    private String communityName;

    /**
     * 社区热度
     */
    private Long communityHot;


    /**
     * 头像链接
     */
    private String profilePhoto;

    /**
     * 社区人数
     */
    @TableField(exist = false)
    private Integer communityCount;

    /**
     * 社区对应的课程id
     */
    private Integer courseId;

    public StudyCommunity(Integer createId, String communityType, String communityIntroduce, String communityName, Long communityHot, Integer courseId) {
        this.createId = createId;
        this.communityType = communityType;
        this.communityIntroduce = communityIntroduce;
        this.communityName = communityName;
        this.communityHot = communityHot;
        this.courseId = courseId;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}