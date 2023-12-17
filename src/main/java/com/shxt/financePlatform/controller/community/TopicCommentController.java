package com.shxt.financePlatform.controller.community;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.entity.TopicComment;
import com.shxt.financePlatform.service.TopicCommentService;
import com.shxt.financePlatform.utils.AuthenticationUtils;
import com.shxt.financePlatform.utils.UploadUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zt
 * @create 2023-11-03 22:59
 * 社区话题回复
 */
@RestController
public class TopicCommentController {

    @Resource
    private TopicCommentService topicCommentService;      //话题评论事务

    @Resource
    private UploadUtils uploadUtils;           //文件事务

    /**
     * 评论话题
     * @param topicComment (topicId、commentContent、fileUrls)
     * @return
     */
    @PostMapping("/replyTopic")
    public R<String> replyTopic(@RequestBody TopicComment topicComment){
        //设置基础信息
        topicComment.setDislikeSum(0);
        topicComment.setLikeSum(0);
        topicComment.setSendTime(new Date());
        topicComment.setUserId(AuthenticationUtils.getUserId());
        topicCommentService.save(topicComment);
        return R.success(null,"评论成功");
    }


    /**
     * 回复评论
     * @param topicComment (commentContent、fileUrls、repliedId)
     * @return
     */
    @PostMapping("/replyComment")
    public R<String> replyComment(@RequestBody TopicComment topicComment){
        //设置根评论
        TopicComment RepliedComment = topicCommentService.getById(topicComment.getRepliedId());
        if(RepliedComment.getFatherId() == null){
            topicComment.setFatherId(topicComment.getRepliedId());
        }else {
            topicComment.setFatherId(RepliedComment.getFatherId());
        }
        //设置基础信息
        topicComment.setDislikeSum(0);
        topicComment.setLikeSum(0);
        topicComment.setSendTime(new Date());
        topicComment.setUserId(AuthenticationUtils.getUserId());
        topicCommentService.save(topicComment);
        return R.success(null,"回复成功");
    }


    /**
     * 删除评论或回复
     * @param commentId 评论/回复id
     * @return
     */
    @DeleteMapping("/topicComment")
    public R<String> deleteComment(@RequestParam Integer commentId){
        List<TopicComment> topicComments = new ArrayList<>();
        TopicComment topicComment = topicCommentService.getById(commentId);
        if (topicComment.getTopicId() != null){   //这是一级评论(需要删除其下的全部子回复)
            topicComments = topicCommentService.getSonTopicComment(commentId);
        }
        topicComments.add(topicComment);
        //遍历删除
        for (TopicComment toc : topicComments){
            String fileUrls = toc.getFileUrls();
            if (fileUrls != null && !"".equals(fileUrls)){
                String[] split = fileUrls.split(",");
                for (String url : split){
                    try {
                        uploadUtils.deleteFile(url);
                    } catch (IOException e) {
                        return R.err("服务器异常,删除失败");
                    }
                }
            }
            topicCommentService.removeById(toc.getCommentId());
        }
        return R.success(null,"删除成功");
    }

    /**
     * 分页获取某条话题下的评论 (包含评论人头像和名字)
     * @param topicId 话题id
     * @return
     */
    @GetMapping("/topicComment")
    public R<IPage<TopicComment>> getTopicComment(@RequestParam Integer topicId, @RequestParam Integer current, @RequestParam Integer size){
        IPage<TopicComment> topicComment = topicCommentService.getTopicComment(topicId, current, size);
        //给评论赋值回复信息
        for (TopicComment record : topicComment.getRecords()) {
            record.setReplyPage(topicCommentService.getCommentReplyPage(record.getCommentId(),1, 4));
        }
        return R.success(topicComment,null);
    }


    /**
     * 获取某条评论下的全部回复(包含评论人头像和名字 被回复人的名字)
     * @param commentId (评论id)
     * @return
     */
    @GetMapping("/commentReply")
    public R<List<TopicComment>> getCommentReply(@RequestParam Long commentId){
        List<TopicComment> commentReply = topicCommentService.getCommentReply(commentId);
        return R.success(commentReply,null);
    }

}
