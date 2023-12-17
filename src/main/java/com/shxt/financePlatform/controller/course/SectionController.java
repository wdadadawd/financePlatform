package com.shxt.financePlatform.controller.course;

import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.entity.SubjectSection;
import com.shxt.financePlatform.service.SubjectSectionService;
import com.shxt.financePlatform.utils.DateUtils;
import com.shxt.financePlatform.utils.UploadUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author zt
 * @create 2023-10-27 21:46
 */
@RestController
public class SectionController {

    @Resource
    private SubjectSectionService subjectSectionService;

    @Resource
    private UploadUtils uploadUtils;

    /**
     * 添加一个专题小节
     * @param subjectSection (subjectId,sectionName,sectionCategory,startTime)
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @PostMapping("/subjectSection")
    public R<Integer> insertSection(@RequestBody SubjectSection subjectSection){
        subjectSectionService.save(subjectSection);
        return R.success(subjectSection.getSectionId(),"添加成功");
    }


    /**
     * 删除一个专题小节
     * @param sectionId 小节id
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @DeleteMapping("/subjectSection")
    public R<String> deleteSection(@RequestParam Integer sectionId){
        subjectSectionService.removeById(sectionId);
        return R.success(null,"删除成功");
    }


    /**
     * 一键设置小节开始时间
     * @param courseId 课程id
     * @param intervalDays 小节开始间隔天数
     * @param startTime 第一个小节开始时间
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @PutMapping("/setAllSectionTime")
    public R<String> oneClickSetSectionTime(@RequestParam Integer courseId, @RequestParam Integer intervalDays,
                                            @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")@RequestParam Date startTime){
        for (SubjectSection courseSection : subjectSectionService.getCourseSections(courseId)) {
            if ("直播".equals(courseSection.getSectionCategory())){
                courseSection.setStartTime(startTime);
                subjectSectionService.updateById(courseSection);
                startTime = DateUtils.getNDaysDate(intervalDays,startTime);
            }
        }
        return R.success(null,"一键设置成功");
    }


    /**
     * 修改专题小节
     * @param subjectSection (sectionId,sectionName,sectionCategory,startTime)
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @PutMapping("/subjectSection")
    public R<String> updateSection(@RequestBody SubjectSection subjectSection){
        subjectSectionService.updateById(subjectSection);
        return R.success(null,"修改成功");
    }


    /**
     * 上传小节直播回放或视频
     * @param file 视频文件
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @PostMapping("/sectionVideo")
    public R<String> insertSectionVideo(@RequestParam("file") MultipartFile file,@RequestParam Integer sectionId){
        String module = "avatar/course/video";
        String videoUrl = subjectSectionService.getById(sectionId).getVideoUrl();
        try {
            String uploadUrl = uploadUtils.uploadFile(file, module);
            if (videoUrl != null && !videoUrl.equals(""))
                uploadUtils.deleteFile(videoUrl);           //删除旧图片
            //更新数据库
            SubjectSection subjectSection = new SubjectSection(sectionId);
            subjectSection.setVideoUrl(uploadUrl);
            subjectSectionService.updateById(subjectSection);
            //返回R对象
            return R.success(uploadUrl,"上传成功");
        } catch (IOException e) {
            return R.err("上传错误");
        }
    }


    /**
     * 获取专题小节
     * @param subjectId 小节id
     * @return
     */
    @GetMapping("/subjectAllSection")
    public R<List<SubjectSection>> getSubjectSection(@RequestParam Integer subjectId){
        List<SubjectSection> subjectSections = subjectSectionService.getSubjectSections(subjectId);
        return R.success(subjectSections,null);
    }


    /**
     * 获取专题的直播小节
     * @param subjectId 小节id
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @GetMapping("/getLiveSection")
    public R<List<SubjectSection>> getLiveSection(@RequestParam Integer subjectId){
        List<SubjectSection> subjectSections = subjectSectionService.getLiveSections(subjectId);
        return R.success(subjectSections,null);
    }

}
