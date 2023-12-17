package com.shxt.financePlatform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shxt.financePlatform.entity.CommunityUserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shxt.financePlatform.entity.StudyCommunity;

/**
* @author 30567
* @description 针对表【community_usre_info】的数据库操作Service
* @createDate 2023-11-02 19:35:11
*/
public interface CommunityUserInfoService extends IService<CommunityUserInfo> {

    void updateByUserIdAndCommunityId(CommunityUserInfo communityUserInfo);

    void deleteByUserIdAndCommunityId(CommunityUserInfo communityUserInfo);

    Boolean judgeApplyCommunity(Integer userId, Integer communityId);

    Integer getCommunityCount(Integer communityId);

    IPage<StudyCommunity> getCommunity(Integer userId, String type, Integer current, Integer size);
}
