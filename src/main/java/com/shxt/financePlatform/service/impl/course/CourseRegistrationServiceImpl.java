package com.shxt.financePlatform.service.impl.course;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shxt.financePlatform.entity.CourseRegistration;
import com.shxt.financePlatform.service.CourseRegistrationService;
import com.shxt.financePlatform.mapper.CourseRegistrationMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 30567
* @description 针对表【course_registration】的数据库操作Service实现
* @createDate 2023-10-25 19:34:52
*/
@Service
public class CourseRegistrationServiceImpl extends ServiceImpl<CourseRegistrationMapper, CourseRegistration>
    implements CourseRegistrationService{

    @Resource
    private CourseRegistrationMapper courseRegistrationMapper;

    /**
     * 删除用户报名的课程
     * @param courseRegistration 用户报名课程信息
     */
    @Override
    public void deleteByClientIdAndCourse(CourseRegistration courseRegistration){
        QueryWrapper<CourseRegistration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("client_id",courseRegistration.getClientId());
        queryWrapper.eq("course_id",courseRegistration.getCourseId());
        courseRegistrationMapper.delete(queryWrapper);
    }

    /**
     * 判断用户是否报名课程
     * @param courseRegistration 用户报名课程信息
     * @return
     */
    @Override
    public boolean judgeApplyCourse(CourseRegistration courseRegistration){
        QueryWrapper<CourseRegistration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("client_id",courseRegistration.getClientId());
        queryWrapper.eq("course_id",courseRegistration.getCourseId());
        return courseRegistrationMapper.selectOne(queryWrapper) != null;
    }


    /**
     * 获取报名课程的人的openId
     * @param courseId 课程id
     * @return
     */
    @Override
    public List<String> getCourseClientOpenId(Integer courseId){
        return courseRegistrationMapper.getCourseClientOpenId(courseId);
    }

}




