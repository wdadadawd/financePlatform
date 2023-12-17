package com.shxt.financePlatform.controller;

import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.utils.UploadUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author zt
 * @create 2023-11-02 16:52
 */
@RestController
public class FileController {

    @Resource
    private UploadUtils uploadUtils;

    /**
     * 上传文件到服务器
     * @param file 文件
     * @return 文件地址
     */
    @PostMapping("/uploadFile")
    public R<String> uploadToServer(@RequestParam("file") MultipartFile file){
        String module = "avatar/community/topic";
        try {
            String uploadUrl = uploadUtils.uploadFile(file, module);
            //返回R对象
            return R.success(uploadUrl,"文件上传成功");
        } catch (IOException e) {
            return R.err("文件上传错误");
        }
    }

    /**
     * 删除服务器文件
     * @param fileUrl 文件地址
     * @return 删除上传到服务器的文件
     */
    @DeleteMapping("/removeFile")
    public R<String> removeFile(@RequestParam("fileUrl") String fileUrl) throws IOException {
        try {
            uploadUtils.deleteFile(fileUrl);
        } catch (IOException e) {
            return R.err("删除失败");
        }
        return R.success(null,"删除成功");
    }
}
