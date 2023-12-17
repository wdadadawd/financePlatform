package com.shxt.financePlatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @TableName live_information
 */
@TableName(value ="live_information")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiveInformation implements Serializable {
    /**
     * 直播记录id
     */
    @TableId(type = IdType.AUTO)
    private Integer liveId;

    /**
     * 频道名
     */
    private String channelName;

    /**
     * 推流服务器地址
     */
    private String serverUrl;

    /**
     * 推流码
     */
    private String rtmpCode;

    /**
     * 回放名
     */
    private String playBackName;

    /**
     * 开始时间戳
     */
    private Long startTime;

    /**
     * 结束时间戳
     */
    private Long endTime;

    /**
     * 失效时间戳
     */
    private Long disabledTime;

    /**
     * 直播日期
     */
    private Date liveDate;

    /**
     * 直播地址
     */
    private String playUrl;

    /**
     * 教师id
     */
    private Integer teacherId;

    /**
     * 直播标题
     */
    private String liveTitle;


    /**
     * 课程id
     */
    private Integer courseId;

    /**
     * 主题id
     */
    private Integer subjectId;

    /**
     * 小节id
     */
    private Integer SectionId;

    /**
     * 课程信息
     */
    @TableField(exist = false)
    private ClientCourseVo courseInfo;

    public LiveInformation(Integer liveId, String channelName, String serverUrl, String rtmpCode, String playBackName, Long startTime, Long endTime, Long disabledTime, Date liveDate, String playUrl, Integer teacherId, String liveTitle, Integer courseId, Integer subjectId, Integer sectionId) {
        this.liveId = liveId;
        this.channelName = channelName;
        this.serverUrl = serverUrl;
        this.rtmpCode = rtmpCode;
        this.playBackName = playBackName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.disabledTime = disabledTime;
        this.liveDate = liveDate;
        this.playUrl = playUrl;
        this.teacherId = teacherId;
        this.liveTitle = liveTitle;
        this.courseId = courseId;
        this.subjectId = subjectId;
        SectionId = sectionId;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}