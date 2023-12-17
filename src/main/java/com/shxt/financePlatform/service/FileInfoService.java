package com.shxt.financePlatform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shxt.financePlatform.entity.FileInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 30567
* @description 针对表【file_info】的数据库操作Service
* @createDate 2023-11-01 20:31:06
*/
public interface FileInfoService extends IService<FileInfo> {


    Page<FileInfo> getFirstFile(Integer courseId, Integer current, Integer size);

    List<FileInfo> getAllFirstFile(Integer courseId);

    List<FileInfo> getDirSonFile(Integer fatherId);

    Page<FileInfo> getNFile(Integer fatherId, Integer current, Integer size);
}
