package com.shxt.financePlatform.controller.info;

import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.entity.CertificatedInformation;
import com.shxt.financePlatform.entity.TeacherInfo;
import com.shxt.financePlatform.entity.User;
import com.shxt.financePlatform.service.CertificatedInformationService;
import com.shxt.financePlatform.service.TeacherInfoService;
import com.shxt.financePlatform.service.UserService;
import com.shxt.financePlatform.utils.AuthenticationUtils;
import com.shxt.financePlatform.utils.UploadUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author zt
 * @create 2023-10-22 20:12
 */
@RestController
public class TeacherInfoController {
    @Resource
    private TeacherInfoService teacherInfoService;

    @Resource
    private UploadUtils uploadUtils;

    @Resource
    private CertificatedInformationService certInfoService;

    @Resource
    private UserService userService;


    /**
     * 更新教师头像
     * @param file 文件
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @PutMapping("/teacherPhoto")
    public R<String> updatePhoto(@RequestParam("file") MultipartFile file){
        String module = "avatar/photo";
        TeacherInfo teacherInfo = teacherInfoService.getById(AuthenticationUtils.getUserId());
        String oldUrl = teacherInfo.getProfilePhoto();
        try {
            String uploadUrl = uploadUtils.uploadFile(file, module);
            if (oldUrl != null && !oldUrl.equals(""))
                uploadUtils.deleteFile(oldUrl);           //删除旧图片
            //更新数据库
            teacherInfo.setProfilePhoto(uploadUrl);
            teacherInfoService.updateById(teacherInfo);
            User user = new User(AuthenticationUtils.getUserId(),null,uploadUrl);
            userService.updateById(user);
            //返回R对象
            return R.success(uploadUrl,"头像上传成功");
        } catch (IOException e) {
            return R.err("头像上传错误");
        }
    }


    /**
     * 修改教师信息
     * @param teacherInfo (userName、teachingAge、profilePhoto、teachingStyle、teachingQualification、teacherProfile、authenticationState)
     * @return
     */
    @PreAuthorize("hasAnyRole('admin','teacher')")
    @PutMapping("/teacherInfo")
    public R<String> updateTeacherInfo(@RequestBody TeacherInfo teacherInfo){
        Integer userId = AuthenticationUtils.getUserId();
        if (teacherInfo.getUserName()!=null){
            User user = new User(userId,teacherInfo.getUserName(),null);
            userService.updateById(user);
        }
        teacherInfo.setUserId(userId);
        teacherInfoService.updateById(teacherInfo);
        return R.success(null,"更新成功");
    }

    
    /**
     * 获取教师信息
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @GetMapping("/teacherInfo")
    public R<TeacherInfo> getTeacherInfo(){
        return R.success(teacherInfoService.getById(AuthenticationUtils.getUserId()),null);
    }


    /**
     * 申请认证
     * @param files 认证资料
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @PostMapping("/teacherAuth")
    public R<String> sendAuthentication(@RequestParam("files")MultipartFile[] files){
        String module = "avatar/teacherAuth";
        Integer userId = AuthenticationUtils.getUserId();
        for (MultipartFile file: files) {
            try {
                String uploadUrl = uploadUtils.uploadFile(file, module);
                //更新数据库
                CertificatedInformation certificatedInformation = new CertificatedInformation(userId, uploadUrl);
                certInfoService.save(certificatedInformation);
                //返回R对象
            } catch (IOException e) {
                return R.err("认证资料上传失败");
            }
        }
        //更改认证状态
        TeacherInfo teacherInfo = new TeacherInfo(userId, null,null,null);
        teacherInfo.setAuthenticationState("认证中");
        teacherInfoService.updateById(teacherInfo);
        return R.success(null,"认证资料上传成功");
    }

}
