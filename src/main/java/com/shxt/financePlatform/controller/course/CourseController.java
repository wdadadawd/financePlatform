package com.shxt.financePlatform.controller.course;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.entity.*;
import com.shxt.financePlatform.service.*;
import com.shxt.financePlatform.utils.AuthenticationUtils;
import com.shxt.financePlatform.utils.UploadUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * @author zt
 * @create 2023-10-25 19:37
 */
@RestController
public class CourseController {

    @Resource
    private CourseService courseService;         //课程事务

    @Resource
    private UploadUtils uploadUtils;             //文件上传工具类

    @Resource
    private TeacherInfoService teacherInfoService;        //教师信息事务

    @Resource
    private SubjectSectionService subjectSectionService;      //课程小节事务

    @Resource
    private StudyCommunityService studyCommunityService;         //学习社区事务

    @Resource
    private FileInfoService fileInfoService;              //文件信息事务



    /**
     * 创建课程
     * @param course (courseName、courseCategory、literaryCourse)
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @PostMapping("/course")
    public R<String> creatCourse(@RequestBody Course course){
        //判断课程名是否存在
        String courseName = course.getCourseName();
        Course courseByName = courseService.getCourseByName(courseName);
        if (courseByName != null)
            return R.err("课程名已存在");
        //添加课程
        Integer userId = AuthenticationUtils.getUserId();
        course.setTeacherId(userId);
        courseService.save(course);
        //创建课程学习社区
        StudyCommunity studyCommunity = new StudyCommunity(userId, "course", "本社区为课程《" + courseName + "》交流社区",
                courseName + "交流社区", 0L, course.getCourseId());
        studyCommunityService.save(studyCommunity);
        return R.success(null,"添加成功");
    }


    /**
     * 删除课程
     * @param courseId 课程id
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @DeleteMapping("/course")
    public R<String> deleteCourse(@RequestParam Integer courseId){
        //校验身份
        if (!judgeAuth(courseId))
            return R.err("无权限");
        courseService.removeById(courseId);
        //删除课程资料
        LinkedBlockingDeque<FileInfo> linkedBlockingDeque = new LinkedBlockingDeque<>(fileInfoService.getAllFirstFile(courseId));
        //创建队列
        while (!linkedBlockingDeque.isEmpty()){
            FileInfo poll = linkedBlockingDeque.poll();
            //如果是文件夹,获取子文件加入队列
            if (poll.getFileType().equals("dir"))
                linkedBlockingDeque.addAll(fileInfoService.getDirSonFile(poll.getFileId()));
            else
                try {
                    uploadUtils.deleteFile(poll.getFileUrl());   //删除云oss文件
                } catch (IOException e) {
                    return R.err("删除文件错误");
                }
            //删除数据库记录
            fileInfoService.removeById(poll.getFileId());
        }
        return R.success(null,"删除成功");
    }


    /**
     * 修改课程信息
     * @param course (courseId、literaryCourse、newPrice、courseName、textDescription、courseCategory)
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @PutMapping("/course")
    public R<String> updateCourse(@RequestBody Course course){
        //校验身份
        if (!judgeAuth(course.getCourseId()))
            return R.err("无权限");
        //判断课程名是否存在
        String courseName = course.getCourseName();
        //如果修改课程名,课程社区信息也要修改
        if(courseName != null){
            Course courseByName = courseService.getCourseByName(courseName);
            if (courseByName != null && !Objects.equals(courseByName.getCourseId(), course.getCourseId()))
                return R.err("课程名已存在");
            StudyCommunity studyCommunity = new StudyCommunity(null, null, "本社区为课程《" + courseName + "》交流社区",
                    courseName + "交流社区", null,course.getCourseId());
            studyCommunityService.updateCommunityByCourseId(studyCommunity);
        }
        Course byId = courseService.getById(course.getCourseId());
        Double courseOldPrice = byId.getOldPrice();
        //如果有课程本身没有设置价格，则设置价格
        if (courseOldPrice == null && course.getNewPrice() != null) {
            course.setOldPrice(course.getNewPrice());
            course.setNewPrice(null);
            //如果课程已经有新旧价格了，又更新价格了
        }else if (courseOldPrice != null && course.getNewPrice() != null && byId.getNewPrice() != null){
            //将原新价格作为旧价格
            course.setOldPrice(byId.getNewPrice());
        }
        courseService.updateById(course);
        return R.success(null,"修改成功");
    }

    /**
     * 获取课程详细信息
     * @param courseId 课程id
     * @return
     */
    @GetMapping("/courseInfo")
    public R<Map<String,Object>> getCourse(@RequestParam Integer courseId){
        HashMap<String, Object> map = new HashMap<>();
        Course course = courseService.getById(courseId);
        course.setClassHour(courseService.getClassHour(courseId));   //设置课时
        TeacherInfo teacherInfo = teacherInfoService.getById(course.getTeacherId());
        map.put("courseInfo",course);
        map.put("courseTeacherInfo",teacherInfo);
        return R.success(map,"获取成功");
    }


    /**
     * 获取全部课程
     * @param category 类别(直播/视频/直播+视频)
     * @param literary 科目
     * @param size 条数
     * @param current 页码
     * @return
     */
    @GetMapping("/getAllCourse")
    public R<Page<Course>> getAllCourse(@RequestParam(required = false) String category,@RequestParam(required = false) String literary,
                                        @RequestParam Integer size,@RequestParam Integer current){
        if("全部".equals(category))
            category = null;
        if ("全部".equals(literary))
            literary = null;
        Page<Course> allCourse = courseService.getAllCourse(size, current, category, literary);
        List<Course> records = allCourse.getRecords();
        for (int i = 0;i<records.size();i++){
            Course course = records.get(i);
            course.setClassHour(courseService.getClassHour(course.getCourseId()));
        }
        return R.success(allCourse,null);
    }

    /**
     * 发布课程
     * @param courseId 课程id
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @PutMapping("/publishedCourse")
    public R<String> publishedCourse(@RequestParam Integer courseId){
        //校验身份
        if (!judgeAuth(courseId))
            return R.err("无权限");
        Course course = courseService.getById(courseId);
        if (course.getOldPrice() == null)
            return R.err("课程未设置价格");
        for (SubjectSection courseSection : subjectSectionService.getCourseSections(courseId)) {
            if ("直播".equals(courseSection.getSectionCategory()) && courseSection.getStartTime() == null)
                return R.err(courseSection.getSectionName() + ",直播小节未设置直播开始时间");
        }
        course = new Course(courseId);
        course.setPublished(true);
        course.setPublishedDate(new Date());
        courseService.updateById(course);
        return R.success(null,"发布成功");
    }

    /**
     * 上传课程封面
     * @param file 图片文件
     * @param courseId 课程id
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @PutMapping("/courseCover")
    public R<String> updateCourseCover(@RequestParam("file") MultipartFile file, @RequestParam Integer courseId){
        //检验身份
        if (!judgeAuth(courseId))
            return R.err("无权限");
        String module = "avatar/course/cover";
        String courseCover = courseService.getById(courseId).getCourseCover();
        try {
            String uploadUrl = uploadUtils.uploadFile(file, module);
            if (courseCover != null && !courseCover.equals(""))
                uploadUtils.deleteFile(courseCover);           //删除旧封面
            //更新数据库
            //更新社区头像
            StudyCommunity studyCommunity = new StudyCommunity();
            studyCommunity.setProfilePhoto(uploadUrl);
            Integer courseCommunityId = studyCommunityService.getCourseCommunityId(courseId);
            studyCommunity.setCommunityId(courseCommunityId);
            studyCommunityService.updateById(studyCommunity);

            Course course = new Course(courseId);
            course.setCourseCover(uploadUrl);
            courseService.updateById(course);
            //返回R对象
            return R.success(uploadUrl,"封面上传成功");
        } catch (IOException e) {
            return R.err("封面上传错误");
        }
    }


    /**
     * 上传课程描述图片
     * @param file 图片文件
     * @param courseId 课程id
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @PutMapping("/courseDesc")
    public R<String> updateCoursePictureDesc(@RequestParam("file") MultipartFile file, @RequestParam Integer courseId){
        //检验身份
        if (!judgeAuth(courseId))
            return R.err("无权限");
        String module = "avatar/course/desc";
        String pictureDesc = courseService.getById(courseId).getPictureDescription();
        try {
            String uploadUrl = uploadUtils.uploadFile(file, module);
            if (pictureDesc != null && !pictureDesc.equals(""))
                uploadUtils.deleteFile(pictureDesc);           //删除旧图片
            //更新数据库
            Course course = new Course(courseId);
            course.setPictureDescription(uploadUrl);
            courseService.updateById(course);
            //返回R对象
            return R.success(uploadUrl,"上传成功");
        } catch (IOException e) {
            return R.err("上传错误");
        }
    }


    /**
     * 分页获取我教的课程 (封面、课程名称)
     * @param size 条数
     * @param current 页码
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @GetMapping("/teacherCourse")
    public R<Page<Course>> getTeacherCourse(@RequestParam Integer size,@RequestParam Integer current){
        Integer userId = AuthenticationUtils.getUserId();
        Page<Course> teacherCourse = courseService.getTeacherCourse(userId, size, current);
        return R.success(teacherCourse,null);
    }


    /**
     * 获取可选的直播课
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @GetMapping("/liveCourse")
    public R<List<Course>> getLiveCourse(){
        Integer teacherId = AuthenticationUtils.getUserId();
        List<Course> liveCourse = courseService.getLiveCourse(teacherId);
        return R.success(liveCourse,null);
    }


    /**
     * 校验身份
     * @param courseId 课程id
     * @return
     */
    public Boolean judgeAuth(Integer courseId){
        Integer userId = AuthenticationUtils.getUserId();
        Course byId = courseService.getById(courseId);
        return Objects.equals(userId, byId.getTeacherId());
    }
}
