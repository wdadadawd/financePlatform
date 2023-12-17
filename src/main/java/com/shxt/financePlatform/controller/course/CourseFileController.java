package com.shxt.financePlatform.controller.course;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.entity.FileInfo;
import com.shxt.financePlatform.service.FileInfoService;
import com.shxt.financePlatform.utils.AuthenticationUtils;
import com.shxt.financePlatform.utils.UploadUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author zt
 * @create 2023-11-01 20:32
 */
@RestController
public class CourseFileController {

    @Resource
    private FileInfoService fileInfoService;

    @Resource
    private UploadUtils uploadUtils;


    /**
     * 创建文件夹
     * @param fileInfo (fatherId、fileName、depth)
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @PostMapping("/courseDir")
    public R<String> createDir(@RequestBody FileInfo fileInfo){
        fileInfo.setFileType("dir");        //设置文件类型
        fileInfo.setCreatTime(new Date());   //设置创建时间
        fileInfo.setCreatId(AuthenticationUtils.getUserId());    //设置创建者id
        fileInfoService.save(fileInfo);
        return R.success(null,"创建文件夹成功");
    }


    /**
     * 上传文件
     * @param file 文件
     * @param fatherId 父id
     * @param depth 是否为一级文件
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @PostMapping("/courseFile")
    public R<String> createCourseFile(@RequestParam("file") MultipartFile file, @RequestParam Integer fatherId,
                                      @RequestParam Boolean depth){
        FileInfo fileInfo = new FileInfo(fatherId, depth);
        String module = "avatar/course/data";
        try {
            String uploadUrl = uploadUtils.uploadFile(file, module);
            //更新数据库
            fileInfo.setFileUrl(uploadUrl);
            fileInfo.setCreatTime(new Date());
            fileInfo.setCreatId(AuthenticationUtils.getUserId());
            fileInfo.setFileType(uploadUtils.getFileType(file));
            fileInfo.setFileName(uploadUtils.getFileOriginalName(file));
            fileInfoService.save(fileInfo);
            //返回R对象
            return R.success(uploadUrl,"文件上传成功");
        } catch (IOException e) {
            return R.err("文件上传错误");
        }
    }


    /**
     * 删除文件
     * @param fileId 文件id
     * @return
     * @throws IOException
     */
    @PreAuthorize("hasRole('teacher')")
    @DeleteMapping("/courseFile")
    public R<String> deleteCourseFile(@RequestParam Integer fileId) throws IOException {
        FileInfo fileInfo = fileInfoService.getById(fileId);
        try {
            //删除云oss文件
            uploadUtils.deleteFile(fileInfo.getFileUrl());
            //删除数据库记录
            fileInfoService.removeById(fileId);
        } catch (IOException e) {
            return R.err("删除失败");
        }
        return R.success(null,"删除成功");
    }


    /**
     * 删除文件夹
     * @param fileId 文件夹id
     * @return
     * @throws IOException
     */
    @PreAuthorize("hasRole('teacher')")
    @DeleteMapping("/courseDir")
    public R<String> deleteCourseDir(@RequestParam Integer fileId) {
        FileInfo fileInfo = fileInfoService.getById(fileId);
        LinkedBlockingDeque<FileInfo> linkedBlockingDeque = new LinkedBlockingDeque<>();
        linkedBlockingDeque.offer(fileInfo);
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
     * 修改文件名
     * @param fileInfo （fileId,fileName）
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @PutMapping("/courseFile")
    public R<String> updateCourseFile(@RequestBody FileInfo fileInfo) {
        fileInfoService.updateById(fileInfo);
        return R.success(null,"修改成功");
    }


    /**
     * 分页获取一级文件
     * @param courseId 课程id
     * @return
     */
    @GetMapping("/courseFirstFile")
    public R<Page<FileInfo>> getCourseFirstFile(@RequestParam Integer courseId,@RequestParam Integer current,
                                                @RequestParam Integer size){
        Page<FileInfo> firstFile = fileInfoService.getFirstFile(courseId,current,size);
        return R.success(firstFile,null);
    }


    /**
     * 分页获取N级文件
     * @param dirId 文件夹id
     * @return
     */
    @GetMapping("/courseNFile")
    public R<Page<FileInfo>> getCourseNFile(@RequestParam Integer dirId,@RequestParam Integer current,
                                            @RequestParam Integer size){
        Page<FileInfo> nFile = fileInfoService.getNFile(dirId,current,size);
        return R.success(nFile,null);
    }
}
