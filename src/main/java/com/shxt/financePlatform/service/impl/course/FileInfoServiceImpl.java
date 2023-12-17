package com.shxt.financePlatform.service.impl.course;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shxt.financePlatform.entity.FileInfo;
import com.shxt.financePlatform.service.FileInfoService;
import com.shxt.financePlatform.mapper.FileInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 30567
* @description 针对表【file_info】的数据库操作Service实现
* @createDate 2023-11-01 20:31:06
*/
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo>
    implements FileInfoService{

    @Resource
    private FileInfoMapper fileInfoMapper;


    /**
     * 分页获取课程第一级文件
     * @param courseId
     * @return
     */
    @Override
    public Page<FileInfo> getFirstFile(Integer courseId,Integer current,Integer size){
        Page<FileInfo> page = new Page<>(current, size);
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("father_id",courseId);
        queryWrapper.eq("depth",true);
        queryWrapper.select("file_id","file_type","file_url","creat_id","creat_time","file_name");
        return fileInfoMapper.selectPage(page,queryWrapper);
    }


    /**
     * 获取课程全部第一级文件
     * @param courseId 课程id
     * @return
     */
    @Override
    public List<FileInfo> getAllFirstFile(Integer courseId){
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("father_id",courseId);
        queryWrapper.eq("depth",true);
        return fileInfoMapper.selectList(queryWrapper);
    }


    /**
     * 获取文件夹的子文件
     * @param fatherId
     * @return
     */
    @Override
    public List<FileInfo> getDirSonFile(Integer fatherId){
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("father_id",fatherId);
        queryWrapper.eq("depth",false);
        return fileInfoMapper.selectList(queryWrapper);
    }


    /**
     * 分页获取文件夹的子文件
     * @param fatherId
     * @return
     */
    @Override
    public Page<FileInfo> getNFile(Integer fatherId,Integer current,Integer size){
        Page<FileInfo> page = new Page<>(current, size);
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("father_id",fatherId);
        queryWrapper.eq("depth",false);
        queryWrapper.select("file_id","file_type","file_url","creat_id","creat_time","file_name");
        return fileInfoMapper.selectPage(page,queryWrapper);
    }

}




