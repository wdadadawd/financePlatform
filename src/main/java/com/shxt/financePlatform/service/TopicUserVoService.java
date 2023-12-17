package com.shxt.financePlatform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shxt.financePlatform.entity.ClientInfo;
import com.shxt.financePlatform.entity.TopicUserVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 30567
* @description 针对表【topic_user_vo】的数据库操作Service
* @createDate 2023-11-05 14:50:47
*/
public interface TopicUserVoService extends IService<TopicUserVo> {


    TopicUserVo getTopicInfoById(Integer topicId);

    Page<TopicUserVo> getCommunityTopic(String sortType, Integer communityId, Integer current, Integer size);

    Page<TopicUserVo> getRecommendTopic(ClientInfo clientInfo, Integer current, Integer size);
}
