package com.shxt.financePlatform.service.impl.wx;

import com.alibaba.fastjson.JSONObject;
import com.shxt.financePlatform.entity.UserTicket;
import com.shxt.financePlatform.service.UserTicketService;
import com.shxt.financePlatform.service.WxHttpRequestService;
import com.shxt.financePlatform.utils.DateUtils;
import com.shxt.financePlatform.wx.WxConstants;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author zt
 * @create 2023-11-06 18:51
 */
@Service
public class WxHttpRequestServiceImpl implements WxHttpRequestService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private WxConstants wxConstants;

    @Resource
    private UserTicketService userTicketService;

    /**
     * 获取并设置wx凭证
     */
    @Override
    public void setWxAccessToken() {
        // 获取WxAccessToken
        String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&" +
                "appid=" + wxConstants.APP_ID + "&secret=" + wxConstants.APP_SECRET;
        String accessTokenStr = restTemplate.getForObject(accessTokenUrl, String.class);
        JSONObject jsonObject = JSONObject.parseObject(accessTokenStr);
        wxConstants.setAccessToken(jsonObject.get("access_token").toString());
//        System.out.println("已获取到AccessToken," + wxConstants.getAccessToken());
    }


    /**
     * 获取用户列表
     */
    @Override
    public void getUserList() {
        String UserOpenIdUrl = "https://api.weixin.qq.com/cgi-bin/user/get?" + "access_token=" +
                wxConstants.getAccessToken() + "&next_openid=" + "";
        String userListStr = restTemplate.getForObject(UserOpenIdUrl, String.class);
        System.out.println(userListStr);
    }

    /**
     * 推送上课通知的模板消息
     * @param openId openId
     */
    @Override
    public void publishMessage(String openId, String courseName,String subjectName,String sectionName, Date startTime){
        String publishUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" +
                wxConstants.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBody = "      {\n" +
                "           \"touser\":\"" + openId + "\",\n" +
                "           \"template_id\":\"ExE1BnsuLgKxO5bECyrHU2F3WW3i7npjCq3KitLnpfc\",\n" +
                "           \"data\":{\n" +
                "                   \"courseName\":{\n" +
                "                       \"value\":\"" + courseName + "\"\n" +
//                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"subjectName\":{\n" +
                "                       \"value\":\"" + subjectName + "\"\n" +
//                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"sectionName\":{\n" +
                "                       \"value\":\"" + sectionName + "\"\n" +
//                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"time\": {\n" +
                "                       \"value\":\"" + DateUtils.getDateTime(startTime) + "\"\n" +
//                "                       \"color\":\"#173177\"\n" +
                "                   }\n" +
                "           }\n" +
                "       }";
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(publishUrl, request, String.class);
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        System.out.println(jsonObject);
    }


    /**
     * 获取绑定微信公众号的二维码地址
     */
    @Override
    public String getUserTicket(Integer userId){
        String ticket = "";
        // 查看数据数中是否存储用户的ticket
        UserTicket userTicket = userTicketService.getById(userId);
        //不存在或快过期了,重新创建一个
        if (userTicket == null || userTicket.getExpireTime() < System.currentTimeMillis()/1000 + 60*60){
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            // 设置请求体
            String requestBody = "{\"expire_seconds\": 604800, \"action_name\": \"QR_STR_SCENE\", \"action_info\": " +
                    "{\"scene\": {\"scene_str\": \"binding_" + userId + "\"}}}";  //binding_123
            // 创建 HttpEntity 包含请求头和请求体
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            // 发送 POST 请求
            String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" +
                    wxConstants.getAccessToken();
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            JSONObject jsonObject = JSONObject.parseObject(response.getBody());
//            System.out.println(jsonObject.get("ticket"));
            ticket = jsonObject.getString("ticket");
            //更新或添加数据库中的userTicket
            userTicketService.saveOrUpdate(new UserTicket(userId, ticket, System.currentTimeMillis() / 1000 + 604800));
        }else {
            ticket = userTicket.getTicket();
        }
        return "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket;
    }

}
