package com.shxt.financePlatform.controller.info;

import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.entity.ClientInfo;
import com.shxt.financePlatform.entity.User;
import com.shxt.financePlatform.service.ClientInfoService;
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
public class ClientInfoController {

    @Resource
    private ClientInfoService clientInfoService;

    @Resource
    private UploadUtils uploadUtils;

    @Resource
    private UserService userService;

    /**
     * 更新用户头像
     * @param file 文件
     * @return
     */
    @PreAuthorize("hasRole('client')")
    @PutMapping("/clientPhoto")
    public R<String> updatePhoto(@RequestParam("file") MultipartFile file){
        ClientInfo clientInfo = clientInfoService.getById(AuthenticationUtils.getUserId());
        String oldUrl = clientInfo.getProfilePhoto();
        String module = "avatar/photo";
        try {
            String uploadUrl = uploadUtils.uploadFile(file, module);
            if (oldUrl != null && !oldUrl.equals(""))
                uploadUtils.deleteFile(oldUrl);           //删除旧图片
            //更新数据库
            clientInfo.setProfilePhoto(uploadUrl);
            clientInfoService.updateClientInfo(clientInfo);
            User user = new User(AuthenticationUtils.getUserId(),null,uploadUrl);
            userService.updateById(user);
            //返回R对象
            return R.success(uploadUrl,"头像上传成功");
        } catch (IOException e) {
            return R.err("头像上传错误");
        }
    }


    /**
     * 修改用户信息
     * @param clientInfo (userName、birthday、sex、individuation、position、education、preferredStyle、freeTime、studyTime、positionTarget、studyCategory)
     * @return
     */
    @PreAuthorize("hasRole('client')")
    @PutMapping("/clientInfo")
    public R<String> updateClientInfo(@RequestBody ClientInfo clientInfo){
        Integer userId = AuthenticationUtils.getUserId();
        if (clientInfo.getUserName() != null){
            User user = new User(userId,clientInfo.getUserName(),null);
            userService.updateById(user);
        }
        clientInfo.setUserId(userId);
        clientInfoService.updateClientInfo(clientInfo);
        return R.success(null,"更新成功");
    }


    /**
     * 获取用户信息
     * @return
     */
    @PreAuthorize("hasRole('client')")
    @GetMapping("/clientInfo")
    public R<ClientInfo> getClientInfo(){
        return R.success(clientInfoService.getById(AuthenticationUtils.getUserId()),null);
    }
}
