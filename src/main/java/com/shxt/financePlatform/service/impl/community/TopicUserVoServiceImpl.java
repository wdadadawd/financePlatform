package com.shxt.financePlatform.service.impl.community;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shxt.financePlatform.entity.ClientInfo;
import com.shxt.financePlatform.entity.TopicUserVo;
import com.shxt.financePlatform.service.TopicUserVoService;
import com.shxt.financePlatform.mapper.TopicUserVoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 30567
* @description 针对表【topic_user_vo】的数据库操作Service实现
* @createDate 2023-11-05 14:50:47
*/
@Service
public class TopicUserVoServiceImpl extends ServiceImpl<TopicUserVoMapper, TopicUserVo>
    implements TopicUserVoService{

    @Resource
    private  TopicUserVoMapper topicUserVoMapper;

    /**
     * 获取话题信息
     * @param topicId
     * @return
     */
    @Override
    public TopicUserVo getTopicInfoById(Integer topicId){
        QueryWrapper<TopicUserVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("topic_id",topicId);
        return topicUserVoMapper.selectOne(queryWrapper);
    }

    /**
     * 分页获取最新/最热社区话题
     * @param communityId 社区id
     * @param current 当前页
     * @param size 条数
     * @return
     */
    @Override
    public Page<TopicUserVo> getCommunityTopic(String sortType, Integer communityId, Integer current, Integer size){
        Page<TopicUserVo> page = new Page<>(current, size);
        QueryWrapper<TopicUserVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("community_id",communityId);
        if (sortType.equals("hot")){
            queryWrapper.orderByDesc("topic_hot");
        }else if (sortType.equals("new")){
            queryWrapper.orderByDesc("send_time");
        }
        return topicUserVoMapper.selectPage(page,queryWrapper);
    }

    /**
     * 分页获取推荐话题
     * @param clientInfo 用户信息
     * @param current 当前页
     * @param size 条数
     * @return
     */
    @Override
    public Page<TopicUserVo> getRecommendTopic(ClientInfo clientInfo, Integer current, Integer size){
        Page<TopicUserVo> page = new Page<>(current, size);
        QueryWrapper<TopicUserVo> queryWrapper = new QueryWrapper<>();
        return topicUserVoMapper.selectPage(page,queryWrapper);
    }
}




