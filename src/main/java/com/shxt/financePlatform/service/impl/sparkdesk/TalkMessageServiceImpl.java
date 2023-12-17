package com.shxt.financePlatform.service.impl.sparkdesk;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shxt.financePlatform.entity.TalkMessage;
import com.shxt.financePlatform.service.TalkMessageService;
import com.shxt.financePlatform.mapper.TalkMessageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 30567
* @description 针对表【talk_message】的数据库操作Service实现
* @createDate 2023-11-10 15:56:16
*/
@Service
public class TalkMessageServiceImpl extends ServiceImpl<TalkMessageMapper, TalkMessage>
    implements TalkMessageService{

    @Resource
    private TalkMessageMapper talkMessageMapper;


    /**
     * 获取用户的历史消息
     * @param userId 用户id
     * @return
     */
    @Override
    public List<TalkMessage> getListAllMessageById(Integer userId){
        QueryWrapper<TalkMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        return talkMessageMapper.selectList(queryWrapper);
    }

}




