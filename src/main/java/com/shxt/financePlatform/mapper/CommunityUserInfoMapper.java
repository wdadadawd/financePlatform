package com.shxt.financePlatform.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shxt.financePlatform.entity.CommunityUserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shxt.financePlatform.entity.StudyCommunity;
import org.apache.ibatis.annotations.Select;

/**
* @author 30567
* @description 针对表【community_usre_info】的数据库操作Mapper
* @createDate 2023-11-02 19:35:11
* @Entity com.shxt.financePlatform.entity.CommunityUsreInfo
*/
public interface CommunityUserInfoMapper extends BaseMapper<CommunityUserInfo> {

    /**
     * 获取某社区总人数
     * @param communityId
     * @return
     */
    @Select("SELECT COUNT(*)\n" +
            "FROM topic_user_vo tuv\n" +
            "WHERE tuv.community_id = #{communityId}")
     Integer getCommunityCount(Integer communityId);


    /**
     * 获取用户加入的开放/课程社区
     * @param userId
     * @param type
     * @return
     */
    @Select("SELECT sc.*\n" +
            "FROM community_user_info cui LEFT JOIN study_community sc\n" +
            "ON cui.community_id = sc.community_id\n" +
            "WHERE cui.user_id = #{userId} AND sc.community_type = #{type}")
    IPage<StudyCommunity> getCommunityByType(Integer userId, String type, Page<StudyCommunity> page);


}




