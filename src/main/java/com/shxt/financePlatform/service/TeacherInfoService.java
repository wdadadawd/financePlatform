package com.shxt.financePlatform.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shxt.financePlatform.entity.TeacherInfo;

/**
* @author 30567
* @description 针对表【teacher_info】的数据库操作Service
* @createDate 2023-10-20 21:47:36
*/
public interface TeacherInfoService extends IService<TeacherInfo> {

    TeacherInfo getCourseTeacherInfo(Integer teacherId);
}
