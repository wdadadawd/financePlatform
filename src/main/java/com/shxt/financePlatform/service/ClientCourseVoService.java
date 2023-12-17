package com.shxt.financePlatform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shxt.financePlatform.entity.ClientCourseVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 30567
* @description 针对表【client_course_vo】的数据库操作Service
* @createDate 2023-10-25 21:07:34
*/
public interface ClientCourseVoService extends IService<ClientCourseVo> {

    Page<ClientCourseVo> getClientCourse(Integer clientId, Integer size, Integer current);

    ClientCourseVo getClientCourse(Integer courseId);
}
