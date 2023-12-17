package com.shxt.financePlatform.service.impl.course;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shxt.financePlatform.entity.ClientCourseVo;
import com.shxt.financePlatform.service.ClientCourseVoService;
import com.shxt.financePlatform.mapper.ClientCourseVoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 30567
* @description 针对表【client_course_vo】的数据库操作Service实现
* @createDate 2023-10-25 21:07:34
*/
@Service
public class ClientCourseVoServiceImpl extends ServiceImpl<ClientCourseVoMapper, ClientCourseVo>
    implements ClientCourseVoService{

    @Resource
    private ClientCourseVoMapper courseVoMapper;

    /**
     * 获取报名的课程
     * @param clientId 用户id
     * @param size 条数
     * @param current 页码
     * @return
     */
    @Override
    public Page<ClientCourseVo> getClientCourse(Integer clientId, Integer size, Integer current){
        Page<ClientCourseVo> page = new Page<>(current,size);
        QueryWrapper<ClientCourseVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("client_id",clientId);
        //获取课程封面url和课程名字、课程类别、教师名字、课程id
        queryWrapper.select("course_name","course_cover","teacher_name","literary_course","course_id");
        return courseVoMapper.selectPage(page,queryWrapper);
    }


    /**
     * 获取课程信息 课程封面url和课程名字、教师名字
     * @param courseId 课程id
     * @return
     */
    @Override
    public ClientCourseVo getClientCourse(Integer courseId){
        QueryWrapper<ClientCourseVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.select("course_name","course_cover","teacher_name");
        return courseVoMapper.selectOne(queryWrapper);
    }

}




