package com.shxt.financePlatform.service.impl.course;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shxt.financePlatform.entity.CourseCollect;
import com.shxt.financePlatform.service.CourseCollectService;
import com.shxt.financePlatform.mapper.CourseCollectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 30567
* @description 针对表【course_collect】的数据库操作Service实现
* @createDate 2023-11-02 14:22:21
*/
@Service
public class CourseCollectServiceImpl extends ServiceImpl<CourseCollectMapper, CourseCollect>
    implements CourseCollectService{

    @Resource
    private CourseCollectMapper courseCollectMapper;

    /**
     * 取消收藏
     * @param clientId 用户id
     * @param courseId 课程id
     */
    @Override
    public void deleteCourseCollect(Integer clientId, Integer courseId){
        QueryWrapper<CourseCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("client_id",clientId);
        queryWrapper.eq("course_id",courseId);
        courseCollectMapper.delete(queryWrapper);
    }
}




