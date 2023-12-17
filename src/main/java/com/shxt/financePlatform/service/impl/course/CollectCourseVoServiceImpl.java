package com.shxt.financePlatform.service.impl.course;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shxt.financePlatform.entity.CollectCourseVo;
import com.shxt.financePlatform.service.CollectCourseVoService;
import com.shxt.financePlatform.mapper.CollectCourseVoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 30567
* @description 针对表【collect_course_vo】的数据库操作Service实现
* @createDate 2023-11-02 14:38:56
*/
@Service
public class CollectCourseVoServiceImpl extends ServiceImpl<CollectCourseVoMapper, CollectCourseVo>
    implements CollectCourseVoService{

    @Resource
    private CollectCourseVoMapper collectCourseVoMapper;

    /**
     * 获取收藏的课程
     * @param clientId 用户id
     * @param size 条数
     * @param current 页码
     * @return
     */
    @Override
    public Page<CollectCourseVo> getCollectCourse(Integer clientId, Integer size, Integer current){
        Page<CollectCourseVo> page = new Page<>(current,size);
        QueryWrapper<CollectCourseVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("client_id",clientId);
        //获取课程封面url和课程名字、课程类别、教师名字、课程id
        queryWrapper.select("course_name","course_cover","teacher_name","literary_course","course_id");
        return collectCourseVoMapper.selectPage(page,queryWrapper);
    }
}




