package com.shxt.financePlatform.service;

import com.shxt.financePlatform.entity.LiveCache;

import java.util.Map;

/**
 * @author zt
 * @create 2023-10-30 17:04
 */
public interface OssLiveService {

    void deleteLiveCache(Integer teacherId);

    void changeLiveCacheStatus(LiveCache liveCache);

    boolean JudgeLiving(String LiveUrl);

    void saveLiveCache(LiveCache liveCache);

    LiveCache getTeacherLiveCache(Integer teacherId);

    Map<String,Object> getLiveMessage(String liveChannelName, String playlistName);

    String savePlayBack(String liveChannelName, String playBackName, long startTime, long endTime);
}
