package com.shxt.financePlatform.service.impl.course;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shxt.financePlatform.entity.CourseAppraise;
import com.shxt.financePlatform.service.CourseAppraiseService;
import com.shxt.financePlatform.mapper.CourseAppraiseMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 30567
* @description 针对表【course_appraise】的数据库操作Service实现
* @createDate 2023-11-02 14:22:21
*/
@Service
public class CourseAppraiseServiceImpl extends ServiceImpl<CourseAppraiseMapper, CourseAppraise>
    implements CourseAppraiseService{

    @Resource
    private CourseAppraiseMapper courseAppraiseMapper;

    /**
     * 获取一个评价
     * @param clientId 用户id
     * @param courseId 课程id
     * @return
     */
    @Override
    public CourseAppraise getOneCourseAppraise(Integer clientId,Integer courseId){
        QueryWrapper<CourseAppraise> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("client_id",clientId);
        queryWrapper.eq("course_id",courseId);
        return courseAppraiseMapper.selectOne(queryWrapper);
    }

    /**
     * 分页获取课程评价
     * @param courseId 课程id
     * @param current 当前页
     * @param size 条数
     * @return
     */
    @Override
    public IPage<CourseAppraise> getPageCourseAppraise(Integer courseId, Integer current, Integer size){
        Page<CourseAppraise> page = new Page<>(current, size);
        QueryWrapper<CourseAppraise> queryWrapper = new QueryWrapper<>();
        return courseAppraiseMapper.getPageCourseAppraise(page,courseId);
    }
}




