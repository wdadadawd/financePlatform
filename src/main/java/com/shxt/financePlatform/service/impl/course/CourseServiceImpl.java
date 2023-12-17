package com.shxt.financePlatform.service.impl.course;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shxt.financePlatform.entity.Course;
import com.shxt.financePlatform.service.CourseService;
import com.shxt.financePlatform.mapper.CourseMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 30567
* @description 针对表【course】的数据库操作Service实现
* @createDate 2023-10-25 19:34:52
*/
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course>
    implements CourseService{

    @Resource
    private CourseMapper courseMapper;


    /**
     * 获取教的课程
     * @param teacherId 教师id
     * @param size   课程数
     * @param current 页码
     * @return
     */
    @Override
    public Page<Course> getTeacherCourse(Integer teacherId, Integer size, Integer current){
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id",teacherId);
        queryWrapper.select("course_name","course_cover","course_id");   //获取课程封面url和课程名字
        Page<Course> page = new Page<>(current,size);
        return courseMapper.selectPage(page, queryWrapper);
    }



    /**
     * 获取课时
     * @param courseId 课程id
     * @return
     */
    @Override
    public Integer getClassHour(Integer courseId){
        return courseMapper.getCourseCount(courseId);
    }


    /**
     * 获取全部课程
     * @param size 条数
     * @param current  页码
     * @param category 类别
     * @param literary 科目
     * @return
     */
    @Override
    public Page<Course> getAllCourse(Integer size, Integer current, String category, String literary) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        if (category != null)
            queryWrapper.eq("course_category",category);
        if (literary != null)
            queryWrapper.eq("literary_course",literary);
        queryWrapper.eq("published",1);          //筛选发布的课程
        queryWrapper.select("course_name","course_cover","course_id","old_price","new_price");   //获取课程封面url和课程名字
        Page<Course> page = new Page<>(current,size);
        return courseMapper.selectPage(page, queryWrapper);
    }

    /**
     * 获取可选的直播课
     * @param teacherId
     * @return
     */
    @Override
    public List<Course> getLiveCourse(Integer teacherId){
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        //筛选直播课
        queryWrapper.eq("teacher_id",teacherId);
        queryWrapper.and(q -> q.eq("course_category","直播").or().eq("course_category","直播+视频"));
        //筛选课程id和课程名
        queryWrapper.select("course_id","course_name");
        return courseMapper.selectList(queryWrapper);
    }


    /**
     * 根据课程名查找课程
     * @param courseName 课程名
     * @return
     */
    @Override
    public Course getCourseByName(String courseName) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_name",courseName);
        return courseMapper.selectOne(queryWrapper);
    }
}




