package com.shxt.financePlatform.controller.course;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.entity.ClientCourseVo;
import com.shxt.financePlatform.entity.CommunityUserInfo;
import com.shxt.financePlatform.entity.CourseRegistration;
import com.shxt.financePlatform.service.ClientCourseVoService;
import com.shxt.financePlatform.service.CommunityUserInfoService;
import com.shxt.financePlatform.service.CourseRegistrationService;
import com.shxt.financePlatform.service.StudyCommunityService;
import com.shxt.financePlatform.utils.AuthenticationUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author zt
 * @create 2023-11-03 16:35
 */
@RestController
public class CourseRegistrationController {

    @Resource
    private CourseRegistrationService courseRegistrationService;   //课程报名事务

    @Resource
    private ClientCourseVoService clientCourseVoService;      //用户报名课程vo

    @Resource
    private CommunityUserInfoService communityUserInfoService;  //用户加入社区事务

    @Resource
    private StudyCommunityService studyCommunityService;         //学习社区事务

    /**
     * 报名课程
     * @param courseId 课程id
     * @return
     */
    @PreAuthorize("hasRole('client')")
    @PostMapping("/applyCourse")
    public R<String> applyCourse(@RequestParam Integer courseId){
        //保存用户报名信息
        Integer userId = AuthenticationUtils.getUserId();
        CourseRegistration courseRegistration = new CourseRegistration(courseId, userId, new Date());
        courseRegistrationService.save(courseRegistration);
        //保存用户加入课程社区信息
        Integer communityId = studyCommunityService.getCourseCommunityId(courseId);
        CommunityUserInfo communityUserInfo = new CommunityUserInfo(communityId,userId, new Date(),0,1);
        communityUserInfoService.save(communityUserInfo);
        return R.success(null,"报名成功");
    }


    /**
     * 判断用户是否报名了该课程
     * @param courseId 课程id
     * @return
     */
    @PreAuthorize("hasAnyRole('client','teacher','admin')")
    @GetMapping("/judgeApplyCourse")
    public R<Boolean> judgeApplyCourse(@RequestParam Integer courseId){
        Integer userId = AuthenticationUtils.getUserId();
        boolean b = courseRegistrationService.judgeApplyCourse(new CourseRegistration(courseId, userId, null));
        return R.success(b,null);
    }

    /**
     * 退课
     * @param courseId 课程id
     * @return
     */
    @PreAuthorize("hasRole('client')")
    @DeleteMapping("/exitCourse")
    public R<String> exitCourse(@RequestParam Integer courseId){
        Integer userId = AuthenticationUtils.getUserId();
        //删除课程报名信息
        courseRegistrationService.deleteByClientIdAndCourse(new CourseRegistration(courseId,userId,null));
        //获取课程社区id
        Integer communityId = studyCommunityService.getCourseCommunityId(courseId);
        //删除用户课程社区信息
        communityUserInfoService.deleteByUserIdAndCommunityId(new CommunityUserInfo(communityId,userId));
        return R.success(null,"退课成功");
    }


    /**
     * 分页获取我报名的课程 (封面、课程名称、教师名称)
     * @param size 条数
     * @param current 页码
     * @return
     */
    @PreAuthorize("hasRole('client')")
    @GetMapping("/clientCourse")
    public R<Page<ClientCourseVo>> getClientCourse(@RequestParam Integer size, @RequestParam Integer current){
        Integer userId = AuthenticationUtils.getUserId();
        Page<ClientCourseVo> clientCourse = clientCourseVoService.getClientCourse(userId, size, current);
        return R.success(clientCourse,null);
    }
}
