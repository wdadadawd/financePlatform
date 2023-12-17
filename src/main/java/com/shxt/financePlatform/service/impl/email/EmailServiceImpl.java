package com.shxt.financePlatform.service.impl.email;


import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.service.EmailService;
import com.shxt.financePlatform.utils.RandomUtils;
import com.shxt.financePlatform.utils.RedisCache;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.TimeUnit;

/**
 * redis 工具类
 */
@Component
public class EmailServiceImpl implements EmailService {

    @Resource
    private RedisCache redisCache;

    @Resource
    private JavaMailSenderImpl mailSender;


    private String emailTemplate = "<head> " +
            "    <base target=\"_blank\" /> " +
            "    <style type=\"text/css\">::-webkit-scrollbar{ display: none; }</style> " +
            "    <style id=\"cloudAttachStyle\" type=\"text/css\">#divNeteaseBigAttach, #divNeteaseBigAttach_bak{display:none;}</style> " +
            "    <style id=\"blockquoteStyle\" type=\"text/css\">blockquote{display:none;}</style> " +
            "    <style type=\"text/css\"> " +
            "        body{font-size:14px;font-family:arial,verdana,sans-serif;line-height:1.666;padding:0;margin:0;overflow:auto;white-space:normal;word-wrap:break-word;min-height:100px} " +
            "        td, input, button, select, body{font-family:Helvetica, 'Microsoft Yahei', verdana} " +
            "        pre {white-space:pre-wrap;white-space:-moz-pre-wrap;white-space:-pre-wrap;white-space:-o-pre-wrap;word-wrap:break-word;width:95%} " +
            "        th,td{font-family:arial,verdana,sans-serif;line-height:1.666} " +
            "        img{ border:0} " +
            "        header,footer,section,aside,article,nav,hgroup,figure,figcaption{display:block}" +
            "        blockquote{margin-right:0px}" +
            "    </style>" +
            "</head>" +
            "<body tabindex=\"0\" role=\"listitem\">" +
            "<table width=\"700\" border=\"0\" align=\"center\" cellspacing=\"0\" style=\"width:700px;\">" +
            "    <tbody>" +
            "    <tr>" +
            "        <td>" +
            "            <div style=\"width:700px;margin:0 auto;border-bottom:1px solid #ccc;margin-bottom:30px;\">" +
            "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"700\" height=\"39\" style=\"font:12px Tahoma, Arial, 宋体;\">" +
            "                    <tbody><tr><td width=\"210\"></td></tr></tbody>" +
            "                </table>" +
            "            </div>" +
            "            <div style=\"width:680px;padding:0 10px;margin:0 auto;\">" +
            "                <div style=\"line-height:1.5;font-size:14px;margin-bottom:25px;color:#4d4d4d;\">" +
            "                    <strong style=\"display:block;margin-bottom:15px;\">尊敬的客户：<span style=\"color:#f60;font-size: 16px;\"></span>您好！</strong>" +
            "                    <strong style=\"display:block;margin-bottom:15px;\">" +
            "                        您正在进行<span style=\"color: red\">$(type)</span>操作，请在验证码输入框中输入：<span style=\"color:#f60;font-size: 24px\">$(code)</span>，以完成操作。" +
            "                    </strong>" +
            "                </div>" +
            "                <div style=\"margin-bottom:30px;\">" +
            "                    <small style=\"display:block;margin-bottom:20px;font-size:12px;\">" +
            "                        <p style=\"color:#747474;\">" +
            "                            注意：此操作可能会修改您的密码、登录邮箱或绑定手机。如非本人操作，请及时登录并修改密码以保证帐户安全" +
            "                            <br>（工作人员不会向你索取此验证码，请勿泄漏！)" +
            "                        </p>" +
            "                    </small>" +
            "                </div>" +
            "            </div>" +
            "            <div style=\"width:700px;margin:0 auto;\">" +
            "                <div style=\"padding:10px 10px 0;border-top:1px solid #ccc;color:#747474;margin-bottom:20px;line-height:1.3em;font-size:12px;\">" +
            "                    <p>此为系统邮件，请勿回复<br>" +
            "                        请保管好您的邮箱，避免账号被他人盗用" +
            "                    </p>" +
            "                    <p>财税培训平台</p>" +
            "                </div>" +
            "            </div>" +
            "        </td>" +
            "    </tr>" +
            "    </tbody>" +
            "</table>" +
            "</body>";

    /**
     * 发送验证码
     * @param email 邮箱
     * @param use 用途
     * @param type 标题
     * @return
     */
    @Override
    public R<String> sendEmail(String email, String use, String type) throws MessagingException, MailSendException {
        Integer count = getMailCodeCount(email, use);
        if (count!=null&&count>=5)
            return R.err("发送失败,已达今日上限");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //生成随机验证码
        String code = RandomUtils.getUUID(6);
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        //设置一个html邮件信息
        helper.setText(emailTemplate.replace("$(type)",type).replace("$(code)",code),true);
        //设置邮件主题名
        helper.setSubject("税海学堂验证码");
        //发给谁-》邮箱地址
        helper.setTo(email);
        //谁发的-》发送人邮箱
        helper.setFrom("3056786023@qq.com");
        saveMailCode(email,code,use);
        mailSender.send(mimeMessage);
        return R.success(null,"发送成功");
    }

    /**
     * 获取该邮箱的发送次数
     * @param email 邮箱
     * @param use 用途
     */
    @Override
    public Integer getMailCodeCount(String email, String use){
        return redisCache.getCacheObject(email + "_" + use+ "_count");
    }

    /**
     * 保存该邮箱的验证码
     * @param email 邮箱
     * @param code 验证码
     */
    @Override
    public void saveMailCode(String email, String code, String use){
        //将邮箱验证码以邮件地址为key存入redis,5分钟过期
        redisCache.setCacheObject(email + "_" + use+ "_code", code,5, TimeUnit.MINUTES);
        Integer mailCodeCount = getMailCodeCount(email,use);
        if (mailCodeCount == null){
            //设置邮箱接收验证码的此时,1天后重置
            redisCache.setCacheObject(email + "_" + use+ "_count", 1,1,TimeUnit.DAYS);
        }else{
            Long cacheExpire = redisCache.getCacheExpire(email + "_" + use + "_count");
//            System.out.println(mailCodeCount);
//            System.out.println(expire);
            redisCache.setCacheObject(email + "_" + use+ "_count", mailCodeCount+1,cacheExpire, TimeUnit.SECONDS);
        }
    }

    /**
     * 获取邮箱的验证码
     * @param email 邮箱
     * @param use 用途
     */
    @Override
    public String getMailCode(String email, String use){
        return redisCache.getCacheObject(email + "_" + use+ "_code");
    }

}

