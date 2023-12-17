package com.shxt.financePlatform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shxt.financePlatform.entity.TopicComment;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
* @author 30567
* @description 针对表【topic_comment】的数据库操作Service
* @createDate 2023-11-02 19:35:11
*/
public interface TopicCommentService extends IService<TopicComment> {

    List<TopicComment> getSonTopicComment(Integer fatherId);

    IPage<TopicComment> getTopicComment(@RequestParam Integer topicId, @RequestParam Integer current, @RequestParam Integer size);

    IPage<TopicComment> getCommentReplyPage(@RequestParam Long commentId, @RequestParam Integer current, @RequestParam Integer size);

    List<TopicComment> getCommentReply(Long commentId);
}
