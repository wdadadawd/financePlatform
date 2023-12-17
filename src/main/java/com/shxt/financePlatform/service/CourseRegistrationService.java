package com.shxt.financePlatform.service;

import com.shxt.financePlatform.entity.CourseRegistration;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 30567
* @description 针对表【course_registration】的数据库操作Service
* @createDate 2023-10-25 19:34:52
*/
public interface CourseRegistrationService extends IService<CourseRegistration> {

    void deleteByClientIdAndCourse(CourseRegistration courseRegistration);

    boolean judgeApplyCourse(CourseRegistration courseRegistration);

    List<String> getCourseClientOpenId(Integer courseId);
}
