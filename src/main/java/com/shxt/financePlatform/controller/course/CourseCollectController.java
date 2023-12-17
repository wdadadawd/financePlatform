package com.shxt.financePlatform.controller.course;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.entity.CollectCourseVo;
import com.shxt.financePlatform.entity.CourseCollect;
import com.shxt.financePlatform.service.CollectCourseVoService;
import com.shxt.financePlatform.service.CourseCollectService;
import com.shxt.financePlatform.utils.AuthenticationUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author zt
 * @create 2023-11-02 14:48
 */
@RestController
public class CourseCollectController {

    @Resource
    private CollectCourseVoService collectCourseVoService;

    @Resource
    private CourseCollectService courseCollectService;


    /**
     * 收藏课程
     * @param courseId 课程id
     * @return
     */
    @PreAuthorize("hasRole('client')")
    @PostMapping("/collectCourse")
    public R<String> addCollectCourse(@RequestParam Integer courseId){
        Integer userId = AuthenticationUtils.getUserId();
        CourseCollect courseCollect = new CourseCollect(courseId, userId, new Date());
        courseCollectService.save(courseCollect);
        return R.success(null,"收藏成功");
    }


    /**
     * 取消收藏课程
     * @param courseId 课程id
     * @return
     */
    @PreAuthorize("hasRole('client')")
    @DeleteMapping("/collectCourse")
    public R<String> deleteCollectCourse(@RequestParam Integer courseId){
        Integer userId = AuthenticationUtils.getUserId();
        courseCollectService.deleteCourseCollect(userId,courseId);
        return R.success(null,"取消收藏成功");
    }


    /**
     * 分页获取我的收藏的课程 (封面、课程名称、教师名称)
     * @param size 条数
     * @param current 页码
     * @return
     */
    @PreAuthorize("hasRole('client')")
    @GetMapping("/collectCourse")
    public R<Page<CollectCourseVo>> getClientCourse(@RequestParam Integer size, @RequestParam Integer current){
        Integer userId = AuthenticationUtils.getUserId();
        Page<CollectCourseVo> clientCourse = collectCourseVoService.getCollectCourse(userId, size, current);
        return R.success(clientCourse,null);
    }
}
