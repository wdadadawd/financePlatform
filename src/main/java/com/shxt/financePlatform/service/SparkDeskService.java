package com.shxt.financePlatform.service;


import com.shxt.financePlatform.sparkdesk.RoleContent;

import java.util.List;

/**
 * @author zt
 * @create 2023-11-09 10:04
 */
public interface SparkDeskService {


    String getResponse(String serverUrl, String issue, Integer userId, String model) throws Exception;

    List<RoleContent> getAllHistory(Integer userId);

    List<RoleContent> getHistory(Integer userId);

    // 鉴权方法
    String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception;
}
