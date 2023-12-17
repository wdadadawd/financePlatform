package com.shxt.financePlatform.quartz;

import com.shxt.financePlatform.entity.Course;
import com.shxt.financePlatform.entity.CourseSubject;
import com.shxt.financePlatform.entity.SubjectSection;
import com.shxt.financePlatform.service.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.annotation.Resource;

/**
 * @author zt
 * @create 2023-11-10 21:14
 */
public class CourseReminderJob implements Job {

    @Resource
    private SubjectSectionService subjectSectionService;           //专题事务

    @Resource
    private CourseService courseService;                           //课程事务

    @Resource
    private CourseSubjectService courseSubjectService;             //小节事务

    @Resource
    private WxHttpRequestService wxHttpRequestService;             //微信公众号事务

    @Resource
    private CourseRegistrationService courseRegistrationService;    //课程报名事务

    @Override
    //课程提示定时任务
    public void execute(JobExecutionContext context) throws JobExecutionException {
//        System.out.println("定时任务执行!!!");
        //获取需要提示的小节信息
        for (SubjectSection section : subjectSectionService.getNowRemindSections()) {
            //获取专题信息
            CourseSubject subject = courseSubjectService.getById(section.getSubjectId());
            //获取课程信息
            Course course = courseService.getById(subject.getCourseId());
            //获取报名用户的openId发送微信提示
            for (String openId : courseRegistrationService.getCourseClientOpenId(course.getCourseId())) {
                wxHttpRequestService.publishMessage(openId,course.getCourseName(),subject.getSubjectName(),
                        section.getSectionName(),section.getStartTime());
            }
            //标记提示,防止重复提示
            section.setIsRemind(true);
            subjectSectionService.updateById(section);
        }
    }
}
