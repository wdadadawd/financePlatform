package com.shxt.financePlatform.sparkdesk;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shxt.financePlatform.entity.TalkMessage;
import com.shxt.financePlatform.utils.RandomUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import com.google.gson.Gson;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author zt
 * @create 2023-11-09 21:29
 */
//@ServerEndpoint("/")
public class XfWebSocketClient extends WebSocketClient {

    private static final Gson gson = new Gson();     //JSON解析工具

    private String totalAnswer="";      // 大模型的答案汇总

    private final String issueContent;        //请求的问题

    private final Integer userId;             //用户id

    private final String model;               //模型

    private TalkMessage newMessage;                  //最新消息

    private TalkMessage newAnswer;                   //新回答

    private final List<RoleContent> historyList;           //历史消息


    public XfWebSocketClient(URI serverUri,String issueContent,Integer userId,String model,List<RoleContent> historyList) {
        super(serverUri);                                //请求路径
        this.issueContent = issueContent;                //请求问题
        this.userId = userId;
        this.model = model;
        this.historyList = historyList;
    }

    public String getAnswer(){                          //返回答案
        return this.totalAnswer;
    }

    public TalkMessage getNewMessage() {
        return newMessage;
    }

    public TalkMessage getNewAnswer() {
        return newAnswer;
    }

    @Override
    //webSocket连接时执行的操作
    public void onOpen(ServerHandshake handshakedata) {
//        System.out.println("连接已打开");
        try {
            //1.创建请求参数
            JSONObject requestJson=new JSONObject();
            // (1)header参数
            JSONObject header=new JSONObject();
            header.put("app_id",XfConstants.APP_ID);        //appId
            header.put("uid", UUID.randomUUID().toString().substring(0, 10));                     //用户id
            // (2)parameter参数
            JSONObject parameter=new JSONObject();
            JSONObject chat=new JSONObject();
            chat.put("domain",model);                //版本号
            chat.put("temperature",0.5);                   //结果随机性，取值越高随机性越强即相同的问题得到的不同答案的可能性越高
            chat.put("max_tokens",4096);                   //模型回答的tokens的最大长度
            parameter.put("chat",chat);
            // (3)payload参数
            JSONObject payload=new JSONObject();
            JSONObject message=new JSONObject();
            JSONArray text=new JSONArray();
            // 历史问题获取
            if(historyList.size()>0){
                for(RoleContent talkMessage:historyList){
                    text.add(JSON.toJSON(talkMessage));
                }
            }
            // 添加最新问题
            RoleContent roleContent=new RoleContent("user",issueContent);
            text.add(JSON.toJSON(roleContent));
            this.newMessage = new TalkMessage(RandomUtils.getUUID(8),userId,"user",issueContent,new Date());
            message.put("text",text);
            payload.put("message",message);
            //合成一个参数requestJson
            requestJson.put("header",header);
            requestJson.put("parameter",parameter);
            requestJson.put("payload",payload);
            // 2.发送请求携带requestJson
            send(requestJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(String message) {
        //1.解析返回结果为JsonParse自定义类
        JsonParse myJsonParse = gson.fromJson(message, JsonParse.class);
        if (myJsonParse.header.code != 0) {           //发成错误
            totalAnswer = "系统错误!";            //提示发生错误
            return;
        }
        List<Text> textList = myJsonParse.payload.choices.text;
        for (Text temp : textList) {                 //将每次的返回串拼接在一块
            totalAnswer+=temp.content;
        }
        //判断返回是否结束
        if (myJsonParse.header.status == 2) {        //0为开头、1为中间、2为结尾
            newAnswer = new TalkMessage(RandomUtils.getUUID(8), userId, "assistant", totalAnswer, new Date());
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
    }

    @Override
    public void onError(Exception ex) {
    }

    //返回的json结果拆解
    static class JsonParse {
        Header header;
        Payload payload;
    }

    static class Header {
        int code;
        int status;
        String sid;
    }

    static class Payload {
        Choices choices;
    }

    static class Choices {
        List<Text> text;
    }

    static class Text {
        String role;       //消息对应的角色
        String content;    //消息的内容
    }
}