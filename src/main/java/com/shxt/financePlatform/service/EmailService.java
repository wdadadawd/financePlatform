package com.shxt.financePlatform.service;

import com.shxt.financePlatform.common.R;

import javax.mail.MessagingException;

/**
 * @author zt
 * @create 2023-10-21 14:38
 */
public interface EmailService {

    R<String> sendEmail(String email, String use, String type) throws MessagingException ;

    Integer getMailCodeCount(String email, String use);

    void saveMailCode(String email, String code, String use);

    String getMailCode(String email, String use);
}
