package com.shxt.financePlatform.controller.community;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.entity.CommunityTopic;
import com.shxt.financePlatform.entity.TopicUserVo;
import com.shxt.financePlatform.service.ClientInfoService;
import com.shxt.financePlatform.service.CommunityTopicService;
import com.shxt.financePlatform.service.TopicUserVoService;
import com.shxt.financePlatform.utils.AuthenticationUtils;
import com.shxt.financePlatform.utils.UploadUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

/**
 * @author zt
 * @create 2023-11-03 22:58
 * 社区话题
 */
@RestController
public class CommunityTopicController {

    @Resource
    private CommunityTopicService communityTopicService;

    @Resource
    private UploadUtils uploadUtils;

    @Resource
    private TopicUserVoService topicUserVoService;

    @Resource
    private ClientInfoService clientInfoService;


    /**
     * 根据话题id获取话题详细信息
     * @param topicId
     * @return
     */
    @GetMapping("/topicInfo")
    public R<TopicUserVo> getTopicInfo(@RequestParam Integer topicId){
        TopicUserVo topicInfoById = topicUserVoService.getTopicInfoById(topicId);
        return R.success(topicInfoById,null);
    }


    /**
     * 发表话题
     * @param communityTopic (topicTitle、topicContent、fileUrls、communityId)
     */
    @PostMapping("/communityTopic")
    public R<String> createCommunityTopic(@RequestBody CommunityTopic communityTopic){
        communityTopic.setUserId(AuthenticationUtils.getUserId());  //设置发表人
        communityTopic.setTopicHot(0L);            //设置热度
        communityTopic.setSendTime(new Date());   //设置发表时间
        communityTopic.setLikeSum(0);             //设置点赞数
        communityTopic.setDislikeSum(0);        //设置踩数
        communityTopicService.save(communityTopic);
        return R.success(null,"发表成功");
    }


    /**
     * 删除话题
     * @param topicId 话题id
     * @return
     */
    @DeleteMapping("/communityTopic")
    public R<String> deleteCommunityTopic(@RequestParam Integer topicId){
        CommunityTopic communityTopic = communityTopicService.getById(topicId);
        String fileUrls = communityTopic.getFileUrls();
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
        communityTopicService.removeById(topicId);
        return R.err("删除成功");
    }


    /**
     * 分页获取最新/最热社区话题
     * @param communityId 社区id
     * @param current 当前页
     * @param size 条数
     */
    @GetMapping("/communityTopic")
    public R<Page<TopicUserVo>> getCommunityTopic(@RequestParam(defaultValue = "hot") String sortType, @RequestParam Integer communityId,
                                                  @RequestParam Integer current, @RequestParam Integer size){
        Page<TopicUserVo> communityTopic = topicUserVoService.getCommunityTopic(sortType, communityId, current, size);
        return R.success(communityTopic,null);
    }


    /**
     * 分页获取推荐话题
     * @param current 条数
     * @param size 当前页
     */
    @GetMapping("/recommendTopic")
    public R<Page<TopicUserVo>> getRecommendTopic(@RequestParam Integer current,@RequestParam Integer size){
//        Integer userId = AuthenticationUtils.getUserId();
//        ClientInfo clientInfo = clientInfoService.getById(userId);
        Page<TopicUserVo> communityTopic = topicUserVoService.getRecommendTopic(null,current,size);
        return R.success(communityTopic,null);
    }

}
