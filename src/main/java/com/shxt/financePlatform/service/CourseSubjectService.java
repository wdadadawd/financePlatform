package com.shxt.financePlatform.service;

import com.shxt.financePlatform.entity.CourseSubject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 30567
* @description 针对表【course_subject】的数据库操作Service
* @createDate 2023-10-25 19:34:52
*/
public interface CourseSubjectService extends IService<CourseSubject> {

    List<CourseSubject> getCourseSubjects(Integer courseId);
}
