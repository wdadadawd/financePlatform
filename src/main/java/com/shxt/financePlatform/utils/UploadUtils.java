package com.shxt.financePlatform.utils;

import com.shxt.financePlatform.service.FileService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zt
 * @create 2023-10-27 22:02
 */
@Component
public class UploadUtils {

    @Resource
    private FileService fileService;

    /**
     * 上传文件到指定模块
     * @param file 文件
     * @param module 模块名
     * @return
     * @throws IOException
     */
    public String uploadFile(MultipartFile file, String module) throws IOException {
        InputStream inputStream = file.getInputStream();
        String originalFilename = file.getOriginalFilename();
        return fileService.upload(inputStream, module, originalFilename);
    }


    /**
     * 删除指定url的文件
     * @param fileUrl
     * @throws IOException
     */
    public void deleteFile(String fileUrl) throws IOException {
        fileService.remove(fileUrl);
    }


    /**
     * 获取文件后缀
     * @param file 文件
     * @return
     */
    public String getFileType(MultipartFile file){
        String filename = file.getOriginalFilename();
        assert filename != null;
        return filename.substring(filename.lastIndexOf(".")+1);
    }


    /**
     * 获取文件名
     * @param file 文件
     * @return
     */
    public String getFileOriginalName(MultipartFile file){
        String filename = file.getOriginalFilename();
        assert filename != null;
        return filename.substring(0,filename.lastIndexOf("."));
    }
}
