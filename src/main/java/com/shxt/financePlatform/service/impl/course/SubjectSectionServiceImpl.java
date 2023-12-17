package com.shxt.financePlatform.service.impl.course;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shxt.financePlatform.entity.SubjectSection;
import com.shxt.financePlatform.service.SubjectSectionService;
import com.shxt.financePlatform.mapper.SubjectSectionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 30567
* @description 针对表【subject_section】的数据库操作Service实现
* @createDate 2023-10-25 19:34:53
*/
@Service
public class SubjectSectionServiceImpl extends ServiceImpl<SubjectSectionMapper, SubjectSection>
    implements SubjectSectionService{

    @Resource
    private SubjectSectionMapper subjectSectionMapper;

    /**
     * 获取专题小节
     * @param subjectId 专题id
     * @return
     */
    @Override
    public List<SubjectSection> getSubjectSections(Integer subjectId) {
        QueryWrapper<SubjectSection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("subject_id",subjectId);
        queryWrapper.select("section_id","section_name","video_url","section_category","start_time");
        return subjectSectionMapper.selectList(queryWrapper);
    }


    /**
     * 获取课程所有小节
     * @param courseId 课程id
     * @return
     */
    @Override
    public List<SubjectSection> getCourseSections(Integer courseId) {
        return subjectSectionMapper.getCourseSection(courseId);
    }


    /**
     * 获取专题可直播的小节
     * @param subjectId 专题id
     * @return
     */
    @Override
    public List<SubjectSection> getLiveSections(Integer subjectId) {
        QueryWrapper<SubjectSection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("subject_id",subjectId);
        queryWrapper.eq("section_category","直播");
        queryWrapper.select("section_id","section_name");
        return subjectSectionMapper.selectList(queryWrapper);
    }


    /**
     * 获取当前需要提示的直播课程
     * @return
     */
    @Override
    public List<SubjectSection> getNowRemindSections(){
        return subjectSectionMapper.getNowRemindSections();
    }
}




