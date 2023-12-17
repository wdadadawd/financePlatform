package com.shxt.financePlatform.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shxt.financePlatform.entity.CourseAppraise;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
* @author 30567
* @description 针对表【course_appraise】的数据库操作Mapper
* @createDate 2023-11-02 14:22:21
* @Entity com.shxt.financePlatform.entity.CourseAppraise
*/
public interface CourseAppraiseMapper extends BaseMapper<CourseAppraise> {

    @Select("select course_appraise.*,client_info.user_name\n" +
            "FROM course_appraise LEFT JOIN client_info\n" +
            "ON course_appraise.client_id  = client_info.user_id\n" +
            "WHERE course_id = #{courseId}")
    IPage<CourseAppraise> getPageCourseAppraise(Page<CourseAppraise> page,Integer courseId);
}




