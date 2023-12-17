package com.shxt.financePlatform.sparkdesk;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zt
 * @create 2023-11-09 21:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleContent implements Serializable {

    private String role;           //角色

    private String content;       //内容

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
