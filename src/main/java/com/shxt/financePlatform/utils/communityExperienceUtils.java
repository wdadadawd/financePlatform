package com.shxt.financePlatform.utils;

import com.shxt.financePlatform.entity.CommunityUserInfo;
import com.shxt.financePlatform.service.CommunityUserInfoService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zt
 * @create 2023-11-03 16:57
 * 社区经验工具类
 */
@Component
public class communityExperienceUtils {

    private  Map<Integer,Integer> map;

    @Resource
    private CommunityUserInfoService communityUserInfoService;

    {
        map = new HashMap<>();
        map.put(1,100);
        map.put(2,300);
        map.put(3,800);
        map.put(4,1500);
        map.put(5,5000);
    }

    /**
     * 根据获取的经验更改数据库,并返回提示信息
     * @param communityUserInfo 用户社区信息
     * @param changeExperience 变动经验
     * @return
     */
    public String rankChange(CommunityUserInfo communityUserInfo,Integer changeExperience){
        Integer userGrade = communityUserInfo.getUserGrade();
        Integer userExperience = communityUserInfo.getUserExperience();
        //加上等级
        Boolean isUpgrade = false;
        userExperience = userExperience + changeExperience;
        if (userGrade != 6){     //不是满级
            //循环直到当前经验不大于升级经验
            while (userExperience >= map.get(userGrade)){
                //升级
                userExperience -= map.get(userGrade);
                userGrade++;
                isUpgrade = true;
            }
        }
        //更新用户社区信息
        communityUserInfo.setUserExperience(userExperience);
        communityUserInfo.setUserGrade(userGrade);
        communityUserInfoService.updateByUserIdAndCommunityId(communityUserInfo);
        if (isUpgrade)
            return "恭喜您,升级了!";
        else
            return "经验提升";
    }
}
