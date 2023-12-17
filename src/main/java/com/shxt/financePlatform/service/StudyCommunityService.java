package com.shxt.financePlatform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shxt.financePlatform.entity.ClientInfo;
import com.shxt.financePlatform.entity.StudyCommunity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 30567
* @description 针对表【study_community】的数据库操作Service
* @createDate 2023-11-02 19:35:11
*/
public interface StudyCommunityService extends IService<StudyCommunity> {

    StudyCommunity getStudyCommunityByName(String communityName);

    Integer getCourseCommunityId(Integer courseId);

    void updateCommunityByCourseId(StudyCommunity studyCommunity);

    Page<StudyCommunity> getRecommendCommunity(ClientInfo clientInfo, Integer current, Integer size);

    Page<StudyCommunity> getCreateCommunity(Integer userId, Integer current, Integer size);
}
