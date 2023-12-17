package com.shxt.financePlatform.service.impl.live;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shxt.financePlatform.entity.LiveInformation;
import com.shxt.financePlatform.service.LiveInformationService;
import com.shxt.financePlatform.mapper.LiveInformationMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 30567
* @description 针对表【live_information】的数据库操作Service实现
* @createDate 2023-10-30 20:44:09
*/
@Service
public class LiveInformationServiceImpl extends ServiceImpl<LiveInformationMapper, LiveInformation>
    implements LiveInformationService{

    @Resource
    private LiveInformationMapper liveInformationMapper;

    /**
     * 获取当前正在直播的课程的直播信息(live_id、live_date、course_id)
     * @param clientId 用户id
     * @return
     */
    @Override
    public List<LiveInformation> getClientLivingCourse(Integer clientId){
        long currentTimeMillis = System.currentTimeMillis()/1000;
        return liveInformationMapper.getClientLivingCourse(clientId,currentTimeMillis);
    }

    /**
     * 获取直播详细信息
     */
    @Override
    public LiveInformation getLiveInformation(Integer liveId){
        QueryWrapper<LiveInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("live_id",liveId);
        queryWrapper.select("live_id","live_date","play_url","teacher_id","live_title","section_id","course_id","subject_id");
        return liveInformationMapper.selectOne(queryWrapper);
    }
}




