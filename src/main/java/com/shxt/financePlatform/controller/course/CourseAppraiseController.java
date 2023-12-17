package com.shxt.financePlatform.controller.course;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.entity.CourseAppraise;
import com.shxt.financePlatform.service.CourseAppraiseService;
import com.shxt.financePlatform.utils.AuthenticationUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author zt
 * @create 2023-11-02 14:30
 */
@RestController
public class CourseAppraiseController {

    @Resource
    private CourseAppraiseService courseAppraiseService;

    /**
     * 评价课程
     * @param courseAppraise (courseId、appraiseScore、appraiseMessage)
     * @return
     */
    @PostMapping("/courseAppraise")
    public R<String> addCourseAppraise(@RequestBody CourseAppraise courseAppraise){
        Integer userId = AuthenticationUtils.getUserId();
        CourseAppraise appraise = courseAppraiseService.getOneCourseAppraise(userId, courseAppraise.getCourseId());
        if (appraise != null)
            return R.err("您已经评价过了");
        courseAppraise.setClientId(userId);
        courseAppraise.setAppraiseTime(new Date());
        courseAppraiseService.save(courseAppraise);
        return R.success(null,"评价成功");
    }


    /**
     * 分页获取课程评价
     * @param courseId 课程id
     * @param current 当前页
     * @param size 条数
     * @return
     */
    @GetMapping("/courseAppraise")
    public R<IPage<CourseAppraise>> getCourseAppraise(@RequestParam Integer courseId, @RequestParam Integer current,
                                                     @RequestParam Integer size){
        IPage<CourseAppraise> pageCourseAppraise = courseAppraiseService.getPageCourseAppraise(courseId, current, size);
        return R.success(pageCourseAppraise,null);
    }
}
