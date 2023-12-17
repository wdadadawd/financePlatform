package com.shxt.financePlatform.service;

import com.shxt.financePlatform.entity.LiveInformation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 30567
* @description 针对表【live_information】的数据库操作Service
* @createDate 2023-10-30 20:44:09
*/
public interface LiveInformationService extends IService<LiveInformation> {

    List<LiveInformation> getClientLivingCourse(Integer clientId);

    LiveInformation getLiveInformation(Integer liveId);
}
