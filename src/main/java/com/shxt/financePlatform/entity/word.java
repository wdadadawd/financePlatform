package com.shxt.financePlatform.entity;

import lombok.Data;

/**
 * @author zt
 * @create 2023-11-11 21:09
 */
@Data
public class word {

    /**
     * 词
     */
    private String text;


    /**
     * 大小
     */
    private Integer size;


    /**
     * tag
     */
    private String category;
}
