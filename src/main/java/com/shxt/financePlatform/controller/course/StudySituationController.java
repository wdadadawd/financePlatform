package com.shxt.financePlatform.controller.course;

import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.entity.StudySituation;
import com.shxt.financePlatform.service.StudySituationService;
import com.shxt.financePlatform.utils.AuthenticationUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zt
 * @create 2023-11-02 15:48
 */
@RestController
public class StudySituationController {

    @Resource
    private StudySituationService studySituationService;

    /**
     * 记录本次小节学习时长
     * @return
     */
    @PostMapping("/studySituation")
    public R<String> addStudySituationTime(Integer sectionId,long second){
        Integer userId = AuthenticationUtils.getUserId();
        StudySituation studySituation = studySituationService.getOneStudySituation(sectionId, userId);
        //判断是否为第一次学习
        if (studySituation == null){
            //第一次学习新建学习信息
            studySituation = new StudySituation(userId,sectionId,second);
            studySituationService.save(studySituation);
        }else {
            //否则更新时长
            studySituation.setLearningTime(studySituation.getLearningTime() + second);
            studySituationService.updateStudySituation(studySituation);
        }
        return R.success(null,"已成功记录");
    }



}
