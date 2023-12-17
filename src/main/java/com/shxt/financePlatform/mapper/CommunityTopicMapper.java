package com.shxt.financePlatform.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shxt.financePlatform.entity.CommunityTopic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

/**
* @author 30567
* @description 针对表【community_topic】的数据库操作Mapper
* @createDate 2023-11-02 19:35:11
* @Entity com.shxt.financePlatform.entity.CommunityTopic
*/
public interface CommunityTopicMapper extends BaseMapper<CommunityTopic> {


    /**
     * 获取社区话题
     * @param page 分页信息
     * @param communityId 社区id
     * @return
     */
    @Select("SELECT u.user_name,u.profile_photo,cot.*\n" +
            "FROM community_topic cot LEFT JOIN `user` u\n" +
            "ON cot.user_id = u.user_id\n" +
            "WHERE cot.community_id =  #{communityId}")
    IPage<CommunityTopic> getCommunityTopic(Page<CommunityTopic> page,@RequestParam Integer communityId);
}




