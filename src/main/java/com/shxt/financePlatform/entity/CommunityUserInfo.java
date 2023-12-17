package com.shxt.financePlatform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName community_user_info
 */
@TableName(value ="community_user_info")
@Data
@NoArgsConstructor
public class CommunityUserInfo implements Serializable {
    /**
     * 社区id
     */
    private Integer communityId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 加入时间
     */
    private Date joinTime;

    /**
     * 用户社区经验
     */
    private Integer userExperience;

    /**
     * 用户社区等级
     */
    private Integer userGrade;


    public CommunityUserInfo(Integer communityId, Integer userId) {
        this.communityId = communityId;
        this.userId = userId;
    }

    public CommunityUserInfo(Integer communityId, Integer userId, Date joinTime, Integer userExperience, Integer userGrade) {
        this.communityId = communityId;
        this.userId = userId;
        this.joinTime = joinTime;
        this.userExperience = userExperience;
        this.userGrade = userGrade;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}