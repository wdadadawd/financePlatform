package com.shxt.financePlatform.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shxt.financePlatform.entity.TopicComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 30567
* @description 针对表【topic_comment】的数据库操作Mapper
* @createDate 2023-11-02 19:35:11
* @Entity com.shxt.financePlatform.entity.TopicComment
*/
public interface TopicCommentMapper extends BaseMapper<TopicComment> {


    /**
     * 分页获取话题评论信息(包含评论人头像和名字)
     * @param page 分页数据
     * @param topicId 话题id
     * @return
     */
    @Select("SELECT u.user_name,u.profile_photo,toc.*\n" +
            "FROM topic_comment toc LEFT JOIN `user` u\n" +
            "ON toc.user_id = u.user_id\n" +
            "WHERE toc.topic_id = #{topicId}")
    IPage<TopicComment> getTopicComment(Page<TopicComment> page,Integer topicId);


    /**
     * 获取某评论下的全部回复 (包含评论人头像和名字 已经被回复人的名字)
     * @param commentId 评论id
     * @return
     */
    @Select("SELECT u1.user_name AS user_name,u1.profile_photo,u2.user_name AS replied_name,toc.*\n" +
            "FROM topic_comment toc LEFT JOIN `user` u1\n" +
            "ON toc.user_id = u1.user_id LEFT JOIN topic_comment toc2\n" +
            "ON toc.replied_id = toc2.comment_id LEFT JOIN `user` u2\n" +
            "ON toc2.user_id = u2.user_id\n" +
            "WHERE  toc.father_id = #{commentId}")
    List<TopicComment> getCommentReply(Long commentId);


    /**
     * 分页获取某评论下的部分回复 (包含评论人名字 已经被回复人的名字)
     * @param commentId 评论id
     * @return
     */
    @Select("SELECT u1.user_name AS user_name,u2.user_name AS replied_name,toc.*\n" +
            "FROM topic_comment toc LEFT JOIN `user` u1\n" +
            "ON toc.user_id = u1.user_id LEFT JOIN topic_comment toc2\n" +
            "ON toc.replied_id = toc2.comment_id LEFT JOIN `user` u2\n" +
            "ON toc2.user_id = u2.user_id\n" +
            "WHERE  toc.father_id = #{commentId}")
    IPage<TopicComment> getCommentReplyPage(Long commentId,Page<TopicComment> page);
}




