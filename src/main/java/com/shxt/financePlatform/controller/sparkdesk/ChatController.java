package com.shxt.financePlatform.controller.sparkdesk;


import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.entity.TalkMessage;
import com.shxt.financePlatform.service.SparkDeskService;
import com.shxt.financePlatform.service.TalkMessageService;
import com.shxt.financePlatform.sparkdesk.RoleContent;
import com.shxt.financePlatform.sparkdesk.XfConstants;
import com.shxt.financePlatform.utils.AuthenticationUtils;
import com.shxt.financePlatform.utils.RandomUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author zt
 * @create 2023-11-09 10:04
 */
@RestController
public class ChatController {

    @Resource
    private SparkDeskService sparkDeskService;

    @Resource
    private TalkMessageService talkMessageService;

    /**
     * 获取回答
     * @param issue 问题
     * @return
     * @throws Exception
     */
    @GetMapping("/chat")
    public String getResponse(@RequestParam String issue){
        Integer userId = AuthenticationUtils.getUserId();
        String serverUrl = "https://spark-api.xf-yun.com/v3.1/chat";
        String model = "generalv3";
        try {
            return sparkDeskService.getResponse(serverUrl, issue, userId, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "系统错误";
        }
    }

    /**
     * 添加聊天记录
     * @param messageList
     * @return
     */
    @PostMapping("/addChatMessage")
    public R<String> addChatMessage(@RequestBody ArrayList<RoleContent> messageList){
        Integer userId = AuthenticationUtils.getUserId();
        for(RoleContent roleContent:messageList){
            TalkMessage talkMessage = new TalkMessage(RandomUtils.getUUID(8),userId,roleContent.getRole(),
                    roleContent.getContent(),new Date());
            talkMessageService.save(talkMessage);     //保存聊天记录
        }
        return R.success(null,"添加成功");
    }

    /**
     * 获取全部历史消息记录
     * @return
     */
    @GetMapping("/allHistoryList")
    public R<List<RoleContent>> getAllHistoryList(){
        Integer userId = AuthenticationUtils.getUserId();
        List<RoleContent> allHistory = sparkDeskService.getAllHistory(userId);
        return R.success(allHistory,null);
    }

    /**
     * 获取最大历史消息记录
     * @return
     */
    @GetMapping("/maxHistoryList")
    public R<List<RoleContent>> getMaxHistoryList(){
        Integer userId = AuthenticationUtils.getUserId();
        List<RoleContent> history = sparkDeskService.getHistory(userId);
        return R.success(history,null);
    }

    /**
     * 获取连接地址和appId
     * @return
     */
    @GetMapping("/authMessage")
    public R<Map<String,String>> getAuthMessage(){
        Map<String, String> map = new HashMap<>();
        String serverUrl = "https://spark-api.xf-yun.com/v3.1/chat";
        String authUrl = "";
        try {
            authUrl = sparkDeskService.getAuthUrl(serverUrl, XfConstants.API_KEY, XfConstants.API_SECRET)
                    .replace("https://", "wss://");
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("authUrl",authUrl);
        map.put("appId",XfConstants.APP_ID);
        return R.success(map,null);
    }




}
