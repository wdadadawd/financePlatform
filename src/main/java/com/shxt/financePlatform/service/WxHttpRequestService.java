package com.shxt.financePlatform.service;

import java.util.Date;

/**
 * @author zt
 * @create 2023-11-06 18:50
 */
public interface WxHttpRequestService {


    void setWxAccessToken();

    void getUserList();

    void publishMessage(String openId, String courseName, String subjectName, String sectionName, Date startTime);

    String getUserTicket(Integer userId);
}
