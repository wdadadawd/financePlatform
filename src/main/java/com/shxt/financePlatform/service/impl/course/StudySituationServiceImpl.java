package com.shxt.financePlatform.service.impl.course;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shxt.financePlatform.entity.StudySituation;
import com.shxt.financePlatform.service.StudySituationService;
import com.shxt.financePlatform.mapper.StudySituationMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 30567
* @description 针对表【study_situation】的数据库操作Service实现
* @createDate 2023-11-02 14:22:21
*/
@Service
public class StudySituationServiceImpl extends ServiceImpl<StudySituationMapper, StudySituation>
    implements StudySituationService{

    @Resource
    private StudySituationMapper studySituationMapper;

    /**
     * 查找学习记录
     * @param sectionId 小节id
     * @param clientId 用户id
     * @return
     */
    @Override
    public StudySituation getOneStudySituation(Integer sectionId, Integer clientId){
        QueryWrapper<StudySituation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("section_id",sectionId);
        queryWrapper.eq("client_id",clientId);
        return studySituationMapper.selectOne(queryWrapper);
    }

    /**
     * 更新用户小节学习信息
     * @param studySituation 学习信息
     */
    @Override
    public void updateStudySituation(StudySituation studySituation){
        QueryWrapper<StudySituation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("section_id",studySituation.getSectionId());
        queryWrapper.eq("client_id",studySituation.getClientId());
        studySituationMapper.update(studySituation,queryWrapper);
    }
}




