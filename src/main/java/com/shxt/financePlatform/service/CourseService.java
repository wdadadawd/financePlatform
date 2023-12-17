package com.shxt.financePlatform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shxt.financePlatform.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 30567
* @description 针对表【course】的数据库操作Service
* @createDate 2023-10-25 19:34:52
*/
public interface CourseService extends IService<Course> {

    Page<Course> getTeacherCourse(Integer teacherId, Integer size, Integer current);

    Integer getClassHour(Integer courseId);

    Page<Course> getAllCourse(Integer size, Integer current,String category,String literary);

    List<Course> getLiveCourse(Integer teacherId);

    Course getCourseByName(String courseName);
}
