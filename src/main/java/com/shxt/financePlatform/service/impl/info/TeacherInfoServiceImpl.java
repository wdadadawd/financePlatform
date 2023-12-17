package com.shxt.financePlatform.service.impl.info;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shxt.financePlatform.entity.TeacherInfo;
import com.shxt.financePlatform.service.TeacherInfoService;
import com.shxt.financePlatform.mapper.TeacherInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 30567
* @description 针对表【teacher_info】的数据库操作Service实现
* @createDate 2023-10-20 21:47:36
*/
@Service
public class TeacherInfoServiceImpl extends ServiceImpl<TeacherInfoMapper, TeacherInfo>
    implements TeacherInfoService {

    @Resource
    private TeacherInfoMapper teacherInfoMapper;

    @Override
    public TeacherInfo getCourseTeacherInfo(Integer teacherId){
        QueryWrapper<TeacherInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",teacherId);
        return teacherInfoMapper.selectOne(queryWrapper);
    }

}




