package com.shxt.financePlatform.controller.community;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.entity.CommunityUserInfo;
import com.shxt.financePlatform.entity.StudyCommunity;
import com.shxt.financePlatform.service.CommunityUserInfoService;
import com.shxt.financePlatform.utils.AuthenticationUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author zt
 * @create 2023-11-03 17:23
 * 社区用户
 */
@RestController
public class CommunityUserInfoController {

    @Resource
    private CommunityUserInfoService communityUserInfoService;

    /**
     * 加入一个开放社区
     * @param communityId 社区id
     * @return
     */
    @PostMapping("/applyCommunity")
    public R<String> applyCommunity(@RequestParam Integer communityId){
        Integer userId = AuthenticationUtils.getUserId();
        CommunityUserInfo communityUserInfo = new CommunityUserInfo(communityId,userId, new Date(),0,1);
        communityUserInfoService.save(communityUserInfo);
        return R.success(null,"加入成功");
    }


    /**
     * 判断用户是否加入该社区
     * @param communityId 社区id
     * @return
     */
    @GetMapping("/judgeApplyCommunity")
    public R<Boolean> judgeApplyCommunity(@RequestParam Integer communityId){
        Integer userId = AuthenticationUtils.getUserId();
        Boolean aBoolean = communityUserInfoService.judgeApplyCommunity(userId, communityId);
        return R.success(aBoolean,null);
    }

    /**
     * 退出开放社区
     * @param communityId 社区id
     * @return
     */
    @DeleteMapping("/exitCommunity")
    public R<String> exitCommunity(@RequestParam Integer communityId){
        Integer userId = AuthenticationUtils.getUserId();
        communityUserInfoService.deleteByUserIdAndCommunityId(new CommunityUserInfo(communityId,userId));
        return R.success(null,"退出成功");
    }


    /**
     * 分页获取加入的开放社区
     * @param current 当前页
     * @param size 条数
     * @return
     */
    @GetMapping("/getOpenCommunity")
    public R<IPage<StudyCommunity>> getOpenCommunity(@RequestParam Integer current, @RequestParam Integer size){
        Integer userId = AuthenticationUtils.getUserId();
        IPage<StudyCommunity> openCom = communityUserInfoService.getCommunity(userId, "open", current, size);
        //设置社区人数
        for (StudyCommunity record : openCom.getRecords()) {
            record.setCommunityCount(communityUserInfoService.getCommunityCount(record.getCommunityId()));
        }
        return R.success(openCom,null);
    }


    /**
     * 分页获取加入的课程社区
     * @param current 当前页
     * @param size 条数
     * @return
     */
    @GetMapping("/getCourseCommunity")
    public R<IPage<StudyCommunity>> getCourseCommunity(@RequestParam Integer current,@RequestParam Integer size){
        Integer userId = AuthenticationUtils.getUserId();
        IPage<StudyCommunity> CourseCom = communityUserInfoService.getCommunity(userId, "course", current, size);
        //设置社区人数
        for (StudyCommunity record : CourseCom.getRecords()) {
            record.setCommunityCount(communityUserInfoService.getCommunityCount(record.getCommunityId()));
        }
        return R.success(CourseCom,null);
    }

}
