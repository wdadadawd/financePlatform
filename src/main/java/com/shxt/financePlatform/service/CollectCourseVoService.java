package com.shxt.financePlatform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shxt.financePlatform.entity.CollectCourseVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 30567
* @description 针对表【collect_course_vo】的数据库操作Service
* @createDate 2023-11-02 14:38:56
*/
public interface CollectCourseVoService extends IService<CollectCourseVo> {

    Page<CollectCourseVo> getCollectCourse(Integer clientId, Integer size, Integer current);
}
