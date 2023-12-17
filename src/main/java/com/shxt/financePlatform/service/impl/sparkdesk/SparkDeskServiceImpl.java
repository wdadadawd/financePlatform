package com.shxt.financePlatform.service.impl.sparkdesk;

import com.shxt.financePlatform.entity.TalkMessage;
import com.shxt.financePlatform.service.SparkDeskService;
import com.shxt.financePlatform.service.TalkMessageService;
import com.shxt.financePlatform.sparkdesk.RoleContent;
import com.shxt.financePlatform.sparkdesk.XfWebSocketClient;
import com.shxt.financePlatform.sparkdesk.XfConstants;
import okhttp3.HttpUrl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zt
 * @create 2023-11-09 10:05
 */
@Service
public class SparkDeskServiceImpl implements SparkDeskService {

    @Resource
    private TalkMessageService talkMessageService;

    @Override
    public String getResponse(String serverUrl, String issue, Integer userId, String model) throws Exception {
        //1.获取鉴权路径
        String authUrl = getAuthUrl(serverUrl, XfConstants.API_KEY, XfConstants.API_SECRET);
        //2.替换地址前缀
        String url = authUrl.replace("https://", "wss://");
        XfWebSocketClient client = new XfWebSocketClient(new URI(url),issue,userId,model,getHistory(userId));  //创建webSocket类
        client.connect();                 //3.发起连接
        //直到client结束
        while (!client.isClosed()){
            Thread.sleep(200);
        }
        //保存记录
        talkMessageService.save(client.getNewMessage());
        talkMessageService.save(client.getNewAnswer());
        return client.getAnswer();
    }

    /**
     * 获取全部历史记录
     */
    @Override
    public List<RoleContent> getAllHistory(Integer userId) {  // 由于历史记录最大上线1.2W左右，需要判断是能能加入历史
        List<TalkMessage> listAllMessage = talkMessageService.getListAllMessageById(userId);
        List<RoleContent> historyList = new ArrayList<>();
        int length = 0;
        //筛选最近的少于1W2的历史聊天记录
        for (TalkMessage userMessage:listAllMessage){
            historyList.add(new RoleContent(userMessage.getRole(),userMessage.getContent()));
        }
        return historyList;
    }


    /**
     * 获取近1.2W的历史消息
     * @return
     */
    @Override
    public List<RoleContent> getHistory(Integer userId) {  // 由于历史记录最大上线1.2W左右，需要判断是能能加入历史
        List<TalkMessage> listAllMessage = talkMessageService.getListAllMessageById(userId);
        List<RoleContent> historyList = new ArrayList<>();
        int length = 0;
        //筛选最近的少于1W2的历史聊天记录
        for (int i = listAllMessage.size() - 1;i>=0;i-=2){
            TalkMessage RobtMessage = listAllMessage.get(i);
            TalkMessage userMessage = listAllMessage.get(i - 1);
            //2条2条添加(人/机器)
            if (length + userMessage.getContent().length() + RobtMessage.getContent().length() <= 12000){
                historyList.add(0,new RoleContent(RobtMessage.getRole(),RobtMessage.getContent()));
                historyList.add(0,new RoleContent(userMessage.getRole(),userMessage.getContent()));
            }else
                break;
        }
        return historyList;
    }


    // 鉴权方法
    @Override
    public String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        // 拼接
        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";
        // System.err.println(preStr);
        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);

        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        // System.err.println(sha);
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        // 拼接地址
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().//
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).//
                addQueryParameter("date", date).//
                addQueryParameter("host", url.getHost()).//
                build();

        // System.err.println(httpUrl.toString());
        return httpUrl.toString();
    }
}
