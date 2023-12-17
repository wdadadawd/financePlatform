package com.shxt.financePlatform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName certificated_information
 */
@TableName(value ="certificated_information")
@Data
public class CertificatedInformation implements Serializable {
    /**
     * 教师id
     */
    private Integer teacherId;

    /**
     * 认证资料链接
     */
    private String informationUrl;

    public CertificatedInformation() {
    }

    public CertificatedInformation(Integer teacherId, String informationUrl) {
        this.teacherId = teacherId;
        this.informationUrl = informationUrl;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}