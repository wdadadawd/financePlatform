package com.shxt.financePlatform.controller.course;

import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.entity.CourseSubject;
import com.shxt.financePlatform.service.CourseSubjectService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zt
 * @create 2023-10-27 14:40
 */
@RestController
public class subjectController {

    @Resource
    private CourseSubjectService subjectService;

    /**
     * 添加课程专题
     * @param courseSubject (courseId,subjectName)
     * @return 添加结果
     */
    @PreAuthorize("hasRole('teacher')")
    @PostMapping("/courseSubject")
    public R<Integer> insertSubject(@RequestBody CourseSubject courseSubject){
        subjectService.save(courseSubject);
        return R.success(courseSubject.getSubjectId(),"添加成功");
    }

    /**
     * 删除课程专题
     * @param subjectId 课程专题id
     * @return 删除结果
     */
    @PreAuthorize("hasRole('teacher')")
    @DeleteMapping("/courseSubject")
    public R<String> deleteSubject(@RequestParam Integer  subjectId){
        subjectService.removeById(subjectId);
        return R.success(null,"删除成功");
    }


    /**
     * 修改课程专题名称
     * @param courseSubject (subjectId,subjectName)
     * @return 修改结果
     */
    @PreAuthorize("hasRole('teacher')")
    @PutMapping("/courseSubject")
    public R<String> updateSubject(@RequestBody CourseSubject courseSubject){
        subjectService.updateById(courseSubject);
        return R.success(null,"修改成功");
    }


    /**
     * 获取课程专题
     * @param courseId 课程id
     * @return 专题集合
     */
    @GetMapping("/courseAllSubject")
    public R<List<CourseSubject>> getCourseSubject(@RequestParam Integer  courseId){
        List<CourseSubject> courseSubjects = subjectService.getCourseSubjects(courseId);
        return R.success(courseSubjects,null);
    }
}
