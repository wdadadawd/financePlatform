package com.shxt.financePlatform.config;

import com.shxt.financePlatform.service.WxHttpRequestService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;


/**
 * @author zt
 * @create 2023-07-14 13:57
 * 定时任务配置类
 */
@Configuration
@EnableScheduling
public class ScheduleConfig {

    @Resource
    private WxHttpRequestService wxHttpRequestService;


    /**
     * 每1小时59分重新获取一次AccessToken
     */
    @Scheduled(fixedRate = 2 * 59 * 60 * 1000) // 2小时 = 2 * 60 * 60 * 1000 毫秒
    public void WxAccessTokenScheduled() {
        wxHttpRequestService.setWxAccessToken();
    }
}
