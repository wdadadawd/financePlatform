package com.shxt.financePlatform.service.impl.community;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shxt.financePlatform.entity.CommunityUserInfo;
import com.shxt.financePlatform.entity.StudyCommunity;
import com.shxt.financePlatform.service.CommunityUserInfoService;
import com.shxt.financePlatform.mapper.CommunityUserInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 30567
* @description 针对表【community_usre_info】的数据库操作Service实现
* @createDate 2023-11-02 19:35:11
*/
@Service
public class CommunityUserInfoServiceImpl extends ServiceImpl<CommunityUserInfoMapper, CommunityUserInfo>
    implements CommunityUserInfoService {

    @Resource
    private CommunityUserInfoMapper communityUserInfoMapper;

    /**
     * 根据社区id和用户id变化用户社区信息
     * @param communityUserInfo 用户社区信息
     */
    @Override
    public void updateByUserIdAndCommunityId(CommunityUserInfo communityUserInfo) {
        QueryWrapper<CommunityUserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",communityUserInfo.getUserId());
        queryWrapper.eq("community_id",communityUserInfo.getCommunityId());
        communityUserInfoMapper.update(communityUserInfo,queryWrapper);
    }


    /**
     * 根据社区id和用户id删除用户社区信息
     * @param communityUserInfo 用户社区信息
     */
    @Override
    public void deleteByUserIdAndCommunityId(CommunityUserInfo communityUserInfo) {
        QueryWrapper<CommunityUserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",communityUserInfo.getUserId());
        queryWrapper.eq("community_id",communityUserInfo.getCommunityId());
        communityUserInfoMapper.delete(queryWrapper);
    }


    /**
     * 判断用户是否加入社区
     * @param userId 用户id
     * @param communityId 社区id
     * @return
     */
    @Override
    public Boolean judgeApplyCommunity(Integer userId, Integer communityId){
        QueryWrapper<CommunityUserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("community_id",communityId);
        CommunityUserInfo communityUserInfo = communityUserInfoMapper.selectOne(queryWrapper);
        return communityUserInfo!=null;
    }


    /**
     * 获取社区总人数
     * @param communityId 社区id
     * @return
     */
    @Override
    public Integer getCommunityCount(Integer communityId){
        return communityUserInfoMapper.getCommunityCount(communityId);
    }


    /**
     * 获取用户加入的开放/课程社区
     * @param userId 用户id
     * @param type 社区类型
     * @param current 当前页
     * @param size 条数
     * @return
     */
    @Override
    public IPage<StudyCommunity> getCommunity(Integer userId,String type,Integer current,Integer size){
        Page<StudyCommunity> page = new Page<>(current,size);
        return communityUserInfoMapper.getCommunityByType(userId,type,page);
    }
}




