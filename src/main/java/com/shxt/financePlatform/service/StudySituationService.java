package com.shxt.financePlatform.service;

import com.shxt.financePlatform.entity.StudySituation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 30567
* @description 针对表【study_situation】的数据库操作Service
* @createDate 2023-11-02 14:22:21
*/
public interface StudySituationService extends IService<StudySituation> {

    StudySituation getOneStudySituation(Integer section, Integer clientId);

    void updateStudySituation(StudySituation studySituation);
}
