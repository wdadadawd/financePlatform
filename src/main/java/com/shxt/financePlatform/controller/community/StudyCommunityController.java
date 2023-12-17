package com.shxt.financePlatform.controller.community;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.entity.CommunityUserInfo;
import com.shxt.financePlatform.entity.StudyCommunity;
import com.shxt.financePlatform.service.ClientInfoService;
import com.shxt.financePlatform.service.CommunityUserInfoService;
import com.shxt.financePlatform.service.StudyCommunityService;
import com.shxt.financePlatform.utils.AuthenticationUtils;
import com.shxt.financePlatform.utils.UploadUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

/**
 * @author zt
 * @create 2023-11-03 15:57
 * 社区
 */
@RestController
public class StudyCommunityController {

    @Resource
    private StudyCommunityService studyCommunityService;

    @Resource
    private UploadUtils uploadUtils;

    @Resource
    private ClientInfoService clientInfoService;

    @Resource
    private CommunityUserInfoService communityUserInfoService;

    /**
     * 创建一个开放学习社区
     * @param studyCommunity (communityIntroduce、communityName)
     * @return
     */
    @PostMapping("/studyCommunity")
    public R<String> creatStudyCommunity(@RequestBody StudyCommunity studyCommunity){
        Integer userId = AuthenticationUtils.getUserId();
        //判断社区名是否重复
        if (studyCommunityService.getStudyCommunityByName(studyCommunity.getCommunityName()) != null)
            return R.err("该社区名已存在");
        //创建社区
        studyCommunity.setCommunityHot(0L);     //设置社区初始热度
        studyCommunity.setCreateId(userId); //设置社区创建者id
        studyCommunity.setCommunityType("open");         //设置社区类型
        studyCommunity.setProfilePhoto("https://zt12.oss-cn-beijing.aliyuncs.com/avatar/photo/IMG_20231124_131326.jpg");
        studyCommunityService.save(studyCommunity);
        CommunityUserInfo communityUserInfo = new CommunityUserInfo(studyCommunity.getCommunityId(), userId, new Date(),0,1);
        communityUserInfoService.save(communityUserInfo);
        return R.success(null,"创建成功");
    }


    /**
     * 解散社区
     * @param communityId 社区id
     * @return
     */
    @DeleteMapping("/studyCommunity")
    public R<String> deleteStudyCommunity(@RequestParam Integer communityId){
        //校验身份
        if (!judgeAuth(communityId))
            return R.err("无权限");
        StudyCommunity communityServiceById = studyCommunityService.getById(communityId);
        if (!communityServiceById.getCommunityType().equals("open")){
            return R.err("课程社区不允许手动解散");
        }
        studyCommunityService.removeById(communityId);
        return R.success(null,"解散成功");
    }


    /**
     * 修改社区简介
     * @param studyCommunity (communityId、communityIntroduce)
     * @return
     */
    @PutMapping("/studyCommunity")
    public R<String> updateStudyCommunity(@RequestBody StudyCommunity studyCommunity){
        //校验身份
        if (!judgeAuth(studyCommunity.getCommunityId()))
            return R.err("无权限");
        studyCommunityService.updateById(studyCommunity);
        return R.success(null,"修改成功");
    }



    /** TODO
     * 上传社区头像
     * @param communityId 社区id
     * @param file 文件
     * @return
     */
    @PutMapping("/communityPhoto")
    public R<String> updateCommunityPhoto(@RequestParam Integer communityId, @RequestParam("file")MultipartFile file) {
        //校验身份
        if (!judgeAuth(communityId))
            return R.err("无权限");
        StudyCommunity studyCommunity = studyCommunityService.getById(communityId);
        String oldUrl = studyCommunity.getProfilePhoto();
        String module = "avatar/photo";
        try {
            String uploadUrl = uploadUtils.uploadFile(file, module);
            if (oldUrl != null && !oldUrl.equals(""))
                uploadUtils.deleteFile(oldUrl);           //删除旧图片
            //更新数据库
            studyCommunity.setProfilePhoto(uploadUrl);
            studyCommunityService.updateById(studyCommunity);
            //返回R对象
            return R.success(uploadUrl,"头像上传成功");
        } catch (IOException e) {
            return R.err("头像上传错误");
        }
    }

    /**
     * 获取社区详细信息
     * @param communityId 社区id
     * @return
     */
    @GetMapping("/studyCommunity")
    public R<StudyCommunity> getCommunityInfo(@RequestParam Integer communityId){
        StudyCommunity studyCommunity = studyCommunityService.getById(communityId);
        studyCommunity.setCommunityCount(communityUserInfoService.getCommunityCount(studyCommunity.getCommunityId()));
        return R.success(studyCommunity,null);
    }

    /**
     * 获取用户的推荐社区
     * @param current 页码
     * @param size 条数
     * @return
     */
    @GetMapping("/recommendCommunity")
    public R<Page<StudyCommunity>> getRecommendCommunity(@RequestParam Integer current, @RequestParam Integer size){
        //获取人员个性化信息
//        Integer userId = AuthenticationUtils.getUserId();
//        ClientInfo clientInfo = clientInfoService.getById(userId);
        Page<StudyCommunity> recommendCommunity = studyCommunityService.getRecommendCommunity(null, current, size);
        //设置社区人数
        for (StudyCommunity record : recommendCommunity.getRecords()) {
            record.setCommunityCount(communityUserInfoService.getCommunityCount(record.getCommunityId()));
        }
        return R.success(recommendCommunity,null);
    }


    /**
     * 获取用户的创建的社区
     * @param current 页码
     * @param size 条数
     * @return
     */
    @GetMapping("/createCommunity")
    public R<Page<StudyCommunity>> getCreateCommunity(@RequestParam Integer current, @RequestParam Integer size){
        //获取用户id
        Integer userId = AuthenticationUtils.getUserId();
        Page<StudyCommunity> recommendCommunity = studyCommunityService.getCreateCommunity(userId, current, size);
        //设置社区人数
        for (StudyCommunity record : recommendCommunity.getRecords()) {
            record.setCommunityCount(communityUserInfoService.getCommunityCount(record.getCommunityId()));
        }
        return R.success(recommendCommunity,null);
    }




    //校验身份
    public Boolean judgeAuth(Integer communityId){
        StudyCommunity studyCommunity = studyCommunityService.getById(communityId);
        return Objects.equals(studyCommunity.getCreateId(), AuthenticationUtils.getUserId());
    }

}
