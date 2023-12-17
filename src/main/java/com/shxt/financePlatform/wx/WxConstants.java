package com.shxt.financePlatform.wx;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @Author: asleepyfish
 * @Date: 2022/8/23 18:46
 * @Description: 常量类
 */
@Component
@Data
public class WxConstants {
    /**
     * appId
     */
    public  final String APP_ID = "wx290767293669dab6";

    /**
     * appSecret
     */
    public  final String APP_SECRET = "e7f29e0c1e1ead927e1e71910ad46f4a";

    /**
     * 公众号
     */
    public  final String PUBLIC_ID = "gh_68e437e0a8e1";

    /**
     * token
     */
    public  final String TOKEN = "20021002zt";

    /**
     * 请求api需要的token，开启定时任务，每一个小时获取一次
     */
    public  String accessToken = "";
}
