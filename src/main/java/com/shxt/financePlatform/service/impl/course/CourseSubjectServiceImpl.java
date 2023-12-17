package com.shxt.financePlatform.service.impl.course;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shxt.financePlatform.entity.CourseSubject;
import com.shxt.financePlatform.service.CourseSubjectService;
import com.shxt.financePlatform.mapper.CourseSubjectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 30567
* @description 针对表【course_subject】的数据库操作Service实现
* @createDate 2023-10-25 19:34:52
*/
@Service
public class CourseSubjectServiceImpl extends ServiceImpl<CourseSubjectMapper, CourseSubject>
    implements CourseSubjectService{

    @Resource
    private CourseSubjectMapper courseSubjectMapper;

    /**
     * 获取课程的专题
     * @param courseId 课程id
     * @return
     */
    @Override
    public List<CourseSubject> getCourseSubjects(Integer courseId) {
        QueryWrapper<CourseSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.select("subject_id","subject_name");
        return courseSubjectMapper.selectList(queryWrapper);
    }
}




