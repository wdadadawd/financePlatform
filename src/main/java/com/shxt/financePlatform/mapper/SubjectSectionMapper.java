package com.shxt.financePlatform.mapper;

import com.shxt.financePlatform.entity.SubjectSection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 30567
* @description 针对表【subject_section】的数据库操作Mapper
* @createDate 2023-10-25 19:34:53
* @Entity com.shxt.financePlatform.entity.SubjectSection
*/
public interface SubjectSectionMapper extends BaseMapper<SubjectSection> {

    /**
     * 获取课程全部小节
     * @param courseId
     * @return
     */
    @Select("select ss.*\n" +
            "FROM course_subject cs RIGHT JOIN\n" +
            "subject_section ss ON cs.subject_id = ss.subject_id\n" +
            "WHERE cs.course_id = #{courseId}")
    List<SubjectSection> getCourseSection(Integer courseId);


    /**
     * 获取当前需要提醒的课程直播小节
     * @return
     */
    @Select("SELECT *\n" +
            "FROM subject_section ss\n" +
            "WHERE ss.start_time > NOW()\n" +
            "  AND ss.start_time <= DATE_ADD(NOW(), INTERVAL 10 MINUTE)\n" +
            "\tAND is_remind = 0")
    List<SubjectSection> getNowRemindSections();
}




