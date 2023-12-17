package com.shxt.financePlatform.mapper;

import com.shxt.financePlatform.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
* @author 30567
* @description 针对表【course】的数据库操作Mapper
* @createDate 2023-10-25 19:34:52
* @Entity com.shxt.financePlatform.entity.Course
*/
public interface CourseMapper extends BaseMapper<Course> {

    /**
     * 获取课时
     * @param courseId 课程id
     * @return
     */
    @Select("SELECT COUNT(*)\n" +
            "FROM course_subject cs RIGHT JOIN subject_section ss\n" +
            "ON cs.subject_id = ss.subject_id\n" +
            "WHERE course_id = #{courseId}")
    Integer getCourseCount(Integer courseId);
}




