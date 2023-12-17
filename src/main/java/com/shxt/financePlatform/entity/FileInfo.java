package com.shxt.financePlatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName file_info
 */
@TableName(value ="file_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo implements Serializable {
    /**
     * 文件id
     */
    @TableId(type = IdType.AUTO)
    private Integer fileId;

    /**
     * 文件类型,文件夹为dir
     */
    private String fileType;

    /**
     * 文件链接
     */
    private String fileUrl;

    /**
     * 父文件的课程id(第一层)或文件id
     */
    private Integer fatherId;

    /**
     * 创建者id
     */
    private Integer creatId;

    /**
     * 创建时间
     */
    private Date creatTime;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 深度,true表示为第一层，
     */
    private Boolean depth;

    public FileInfo(Integer fatherId, Boolean depth) {
        this.fatherId = fatherId;
        this.depth = depth;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}