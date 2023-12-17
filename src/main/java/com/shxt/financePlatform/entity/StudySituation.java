package com.shxt.financePlatform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName study_situation
 */
@TableName(value ="study_situation")
@Data
@NoArgsConstructor
public class StudySituation implements Serializable {
    /**
     * 用户id
     */
    private Integer clientId;

    /**
     * 小节id
     */
    private Integer sectionId;

    /**
     * 学习时长
     */
    private Long learningTime;

    /**
     * 是否完成
     */
    private Integer accomplish;

    /**
     * 小节分数
     */
    private Double sectionScore;

    public StudySituation(Integer clientId, Integer sectionId, Long learningTime) {
        this.clientId = clientId;
        this.sectionId = sectionId;
        this.learningTime = learningTime;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}