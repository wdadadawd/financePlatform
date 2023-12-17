package com.shxt.financePlatform.service;

import com.shxt.financePlatform.entity.SubjectSection;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 30567
* @description 针对表【subject_section】的数据库操作Service
* @createDate 2023-10-25 19:34:53
*/
public interface SubjectSectionService extends IService<SubjectSection> {


    List<SubjectSection> getSubjectSections(Integer subjectId);


    List<SubjectSection> getCourseSections(Integer courseId);

    List<SubjectSection> getLiveSections(Integer subjectId);

    List<SubjectSection> getNowRemindSections();
}
