package com.shxt.financePlatform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shxt.financePlatform.entity.CourseAppraise;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 30567
* @description 针对表【course_appraise】的数据库操作Service
* @createDate 2023-11-02 14:22:21
*/
public interface CourseAppraiseService extends IService<CourseAppraise> {

    CourseAppraise getOneCourseAppraise(Integer clientId, Integer courseId);

    IPage<CourseAppraise> getPageCourseAppraise(Integer courseId, Integer current, Integer size);
}
