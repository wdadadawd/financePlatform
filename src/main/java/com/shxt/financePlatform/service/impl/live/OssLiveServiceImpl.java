package com.shxt.financePlatform.service.impl.live;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.*;
import com.shxt.financePlatform.config.OssProperties;
import com.shxt.financePlatform.entity.LiveCache;
import com.shxt.financePlatform.exception.MyException;
import com.shxt.financePlatform.service.OssLiveService;
import com.shxt.financePlatform.utils.RedisCache;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zt
 * @create 2023-10-29 20:31
 */
@Service
public class OssLiveServiceImpl implements OssLiveService {

    @Resource
    private RedisCache redisCache;

    @Resource
    private RestTemplate restTemplate;


    private final int defaultHour = 8;


    /**
     * 删除直播缓存
     * @param teacherId 教师id
     */
    @Override
    public void deleteLiveCache(Integer teacherId){
        String key = "liveCache_" + teacherId;
        redisCache.deleteObject(key);
    }


    /**
     * 更改直播缓存信息为正在直播
     * @param liveCache 缓存信息
     */
    @Override
    public void changeLiveCacheStatus(LiveCache liveCache){
        String key = "liveCache_" + liveCache.getTeacherId();
        liveCache.setStatus("正在直播");
        //转为json字符串
        String s = JSON.toJSONString(liveCache);
        //更改缓存信息
        Long cacheExpire = redisCache.getCacheExpire(key);
        redisCache.setCacheObject(key,s,cacheExpire, TimeUnit.SECONDS);
    }

    /**
     * 判断直播是否已连接
     * @param LiveUrl 观看地址
     * @return
     */
    @Override
    public boolean JudgeLiving(String LiveUrl){
        ResponseEntity<String> response = null;
        try {
            //发送一个请求，根据请求结果判断是否连接
            response = restTemplate.exchange(LiveUrl, HttpMethod.GET, null, String.class);
        } catch (HttpClientErrorException e) {
            return false;
        }
        HttpStatus statusCode = response.getStatusCode();       //请求码
        System.out.println(statusCode);
        return statusCode == HttpStatus.OK;
    }

    /**
     * 缓存用户直播状态
     * @param liveCache 直播信息缓存
     */
    @Override
    public void saveLiveCache(LiveCache liveCache){
        //转为json字符串
        String s = JSON.toJSONString(liveCache);
        //存储缓存为8小时，保存的redis中
        redisCache.setCacheObject("liveCache_" + liveCache.getTeacherId(),s,defaultHour, TimeUnit.HOURS);
    }

    /**
     * 获取用户直播缓存状态
     * @param teacherId  教师id
     * @return
     */
    @Override
    public LiveCache getTeacherLiveCache(Integer teacherId){
        String key = "liveCache_" + teacherId;                      //直播缓存名
        String liveCacheJson = redisCache.getCacheObject(key);      //获取直播缓存
        if (liveCacheJson == null)
            return null;
        return JSON.parseObject(liveCacheJson, LiveCache.class);      //JSON解析成类返回
    }

    /**
     * 获取推流信息
     * @param liveChannelName LiveChannel名称
     * @param playlistName playlistName名称
     * @return
     */
    @Override
    public Map<String,Object> getLiveMessage(String liveChannelName, String playlistName) {
        String bucketName = OssProperties.BUCKET_NAME;  // Bucket名称
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(OssProperties.ENDPOINT, OssProperties.KEY_ID, OssProperties.KEY_SECRET);
        Map<String, Object> map = new HashMap<>();
        try {
            //创建一个申请直播频道的请求  使用bucketName、频道名、频道描述
            CreateLiveChannelRequest request = new CreateLiveChannelRequest(bucketName,
                    liveChannelName, "desc", LiveChannelStatus.Enabled, new LiveChannelTarget());
            CreateLiveChannelResult result = ossClient.createLiveChannel(request);
            // 获取该频道的推流地址。
            List<String> publishUrls = result.getPublishUrls();
            for (String item : publishUrls) {
                // 获取不包含签名信息的推流地址。
//                System.out.println(item);
                // 获取包含签名信息的推流地址。
//                LiveChannelInfo liveChannelInfo = ossClient.getLiveChannelInfo(bucketName, liveChannelName);
                // expires表示过期时间，单位为Unix时间戳。本示例以设置过期时间为1小时为例。
                long now = System.currentTimeMillis() / 1000;
                long expires = now + 3600 * defaultHour;
                //保存推流信息的有效开始时间和失效时间
                map.put("startTime",now);
                map.put("disabledTime",expires);

                //通过bucket名称、频道名、m3u8视频的名称以及过期时间获取一个包含推流地址和推流码的地址
                String signRtmpUrl = ossClient.generateRtmpUri(bucketName, liveChannelName,playlistName, expires);
//                System.out.println(signRtmpUrl);
                map.put("signRtmpUrl",signRtmpUrl);
            }

            // 获取播放地址。
            List<String> playUrls = result.getPlayUrls();
            String s = playUrls.get(0);
            //获取的播放地址有误，自己做了处理
            map.put("playUrl",s.substring(0,s.indexOf(liveChannelName)+liveChannelName.length()+1) + playlistName);
//            for (String item : playUrls) {
//                System.out.println(item);
//            }
        } catch (OSSException oe) {
            throw new MyException("oss异常");
        } catch (ClientException ce) {
            throw new MyException("客户端异常");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return map;
    }

    /**
     * 保存直播回放
     * @param liveChannelName LiveChannel名称
     * @param playBackName 回放名
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @Override
    public String savePlayBack(String liveChannelName, String playBackName, long startTime, long endTime){
        //创建一个oss客户请求类
        OSS ossClient = new OSSClientBuilder().build(OssProperties.ENDPOINT, OssProperties.KEY_ID, OssProperties.KEY_SECRET);
//        ossClient.setBucketAcl(OssProperties.BUCKET_NAME, CannedAccessControlList.PublicRead);
        //Bucket名称、频道名、回放名称、回放开始时间、回放结束时间
        VoidResult result = ossClient.generateVodPlaylist(OssProperties.BUCKET_NAME, liveChannelName, playBackName, startTime, endTime);
        ResponseMessage response = result.getResponse();
        //System.out.println(JSON.toJSONString(response));
        String uri = response.getUri();
        return uri.substring(0,uri.indexOf(".m3u8") + 5);    //获取回放地址
    }



    /**
     * 获取当前在线时间  UTC 格式时间
     * @return
     */
    public static String getTime(){
        Date date = new Date();// 获取当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
        int dstOffset = calendar.get(Calendar.DST_OFFSET);
        calendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        long timeInMillis = calendar.getTimeInMillis();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return df.format(timeInMillis);
    }
}