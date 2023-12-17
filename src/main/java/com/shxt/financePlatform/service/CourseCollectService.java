package com.shxt.financePlatform.service;

import com.shxt.financePlatform.entity.CourseCollect;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 30567
* @description 针对表【course_collect】的数据库操作Service
* @createDate 2023-11-02 14:22:21
*/
public interface CourseCollectService extends IService<CourseCollect> {

    void deleteCourseCollect(Integer clientId, Integer courseId);
}
