package com.shxt.financePlatform.service.impl.community;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shxt.financePlatform.entity.CommunityTopic;
import com.shxt.financePlatform.service.CommunityTopicService;
import com.shxt.financePlatform.mapper.CommunityTopicMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 30567
* @description 针对表【community_topic】的数据库操作Service实现
* @createDate 2023-11-02 19:35:11
*/
@Service
public class CommunityTopicServiceImpl extends ServiceImpl<CommunityTopicMapper, CommunityTopic>
    implements CommunityTopicService{

    @Resource
    private CommunityTopicMapper communityTopicMapper;

}




