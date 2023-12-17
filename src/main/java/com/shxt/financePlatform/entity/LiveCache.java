package com.shxt.financePlatform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zt
 * @create 2023-10-30 17:21
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LiveCache implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 直播记录id
     */
    private Integer liveId;

    /**
     * 失效时间戳
     */
    private long disabledTime;

    /**
     * 直播地址
     */
    private String playUrl;

    /**
     * 推流码
     */
    private String rtmpCode;

    /**
     * 推流服务器
     */
    private String serverUrl;

    /**
     * 教师id
     */
    private Integer teacherId;

    /**
     * 回放名
     */
    private String playBackName;

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
    private Integer sectionId;


    /**
     * 直播标题
     */
    private String liveTitle;

    /**
     * 频道
     */
    private String liveChannelName;

    /**
     * 状态
     */
    private String status;
}
