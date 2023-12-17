package com.shxt.financePlatform.service.impl.community;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shxt.financePlatform.entity.ClientInfo;
import com.shxt.financePlatform.entity.StudyCommunity;
import com.shxt.financePlatform.service.StudyCommunityService;
import com.shxt.financePlatform.mapper.StudyCommunityMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 30567
* @description 针对表【study_community】的数据库操作Service实现
* @createDate 2023-11-02 19:35:11
*/
@Service
public class StudyCommunityServiceImpl extends ServiceImpl<StudyCommunityMapper, StudyCommunity>
    implements StudyCommunityService{

    @Resource
    private StudyCommunityMapper studyCommunityMapper;

    /**
     * 根据社区名搜索社区
     * @param communityName 社区名
     * @return
     */
    @Override
    public StudyCommunity getStudyCommunityByName(String communityName) {
        QueryWrapper<StudyCommunity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("community_name",communityName);
        return studyCommunityMapper.selectOne(queryWrapper);
    }

    /**
     * 获取课程对应的社区id
     * @param courseId 课程id
     * @return
     */
    @Override
    public Integer getCourseCommunityId(Integer courseId){
        QueryWrapper<StudyCommunity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.select("community_id");
        return (Integer) studyCommunityMapper.selectObjs(queryWrapper).get(0);
    }


    /**
     * 根据课程id修改社区信息
     * @param studyCommunity 社区信息
     */
    @Override
    public void updateCommunityByCourseId(StudyCommunity studyCommunity){
        QueryWrapper<StudyCommunity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",studyCommunity.getCourseId());
        studyCommunityMapper.update(studyCommunity,queryWrapper);
    }


    /**
     * 获取用户的推荐社区
     * @param clientInfo 用户信息
     * @param current 页码
     * @param size 条数
     * @return
     */
    @Override
    public Page<StudyCommunity> getRecommendCommunity(ClientInfo clientInfo, Integer current, Integer size){
        QueryWrapper<StudyCommunity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("community_id","community_name","profile_photo","community_hot");
        queryWrapper.isNull("course_id");
        Page<StudyCommunity> page = new Page<>(current,size);
        return studyCommunityMapper.selectPage(page,queryWrapper);
    }

    /**
     * 获取用户的创建的社区
     * @param userId 用户id
     * @param current 页码
     * @param size 条数
     * @return
     */
    @Override
    public Page<StudyCommunity> getCreateCommunity(Integer userId, Integer current, Integer size){
        QueryWrapper<StudyCommunity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("community_id","community_name","profile_photo","community_hot");
        queryWrapper.eq("create_id",userId);
        Page<StudyCommunity> page = new Page<>(current,size);
        return studyCommunityMapper.selectPage(page,queryWrapper);
    }
}




