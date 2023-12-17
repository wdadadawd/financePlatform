package com.shxt.financePlatform.mapper;

import com.shxt.financePlatform.entity.CourseRegistration;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 30567
* @description 针对表【course_registration】的数据库操作Mapper
* @createDate 2023-10-25 19:34:52
* @Entity com.shxt.financePlatform.entity.CourseRegistration
*/
public interface CourseRegistrationMapper extends BaseMapper<CourseRegistration> {

    /**
     * 获取某课程报名人的课程id
     * @param courseId
     * @return
     */
    @Select("SELECT u.open_id\n" +
            "FROM course_registration cr LEFT JOIN `user` u\n" +
            "ON cr.client_id = u.user_id\n" +
            "WHERE cr.course_id = #{courseId} AND u.open_id IS NOT NULL")
    List<String> getCourseClientOpenId(Integer courseId);
}




