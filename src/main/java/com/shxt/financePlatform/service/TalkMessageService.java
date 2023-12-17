package com.shxt.financePlatform.service;

import com.shxt.financePlatform.entity.TalkMessage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 30567
* @description 针对表【talk_message】的数据库操作Service
* @createDate 2023-11-10 15:56:16
*/
public interface TalkMessageService extends IService<TalkMessage> {

    List<TalkMessage> getListAllMessageById(Integer userId);
}
