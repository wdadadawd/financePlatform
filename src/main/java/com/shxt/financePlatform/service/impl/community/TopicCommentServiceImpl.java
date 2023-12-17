package com.shxt.financePlatform.service.impl.community;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shxt.financePlatform.entity.TopicComment;
import com.shxt.financePlatform.service.TopicCommentService;
import com.shxt.financePlatform.mapper.TopicCommentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 30567
* @description 针对表【topic_comment】的数据库操作Service实现
* @createDate 2023-11-02 19:35:11
*/
@Service
public class TopicCommentServiceImpl extends ServiceImpl<TopicCommentMapper, TopicComment>
    implements TopicCommentService{

    @Resource
    private TopicCommentMapper topicCommentMapper;


    /**
     * 获取子评论
     * @param fatherId 父评论
     * @return
     */
    @Override
    public List<TopicComment> getSonTopicComment(Integer fatherId){
        QueryWrapper<TopicComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("father_id", fatherId);
        return topicCommentMapper.selectList(queryWrapper);
    }


    /**
     * 分页获取话题评论信息
     * @param topicId 话题id
     * @param current 当前页
     * @param size 条数
     * @return
     */
    @Override
    public IPage<TopicComment> getTopicComment(Integer topicId,  Integer current,  Integer size){
        Page<TopicComment> page = new Page<>(current,size);
        return topicCommentMapper.getTopicComment(page,topicId);
    }


    /**
     * 分页获取评论部分回复信息
     * @param commentId 评论id
     * @param current 当前页
     * @param size 条数
     * @return
     */
    @Override
    public IPage<TopicComment> getCommentReplyPage( Long commentId,  Integer current,  Integer size){
        Page<TopicComment> page = new Page<>(current,size);
        return topicCommentMapper.getCommentReplyPage(commentId,page);
    }


    /**
     * 获取评论的全部回复
     * @param commentId 评论id
     * @return
     */
    @Override
    public List<TopicComment> getCommentReply(Long commentId){
        return topicCommentMapper.getCommentReply(commentId);
    }

}




