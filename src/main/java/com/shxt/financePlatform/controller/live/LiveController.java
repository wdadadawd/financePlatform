package com.shxt.financePlatform.controller.live;

import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.entity.LiveCache;
import com.shxt.financePlatform.entity.LiveInformation;
import com.shxt.financePlatform.entity.SubjectSection;
import com.shxt.financePlatform.service.ClientCourseVoService;
import com.shxt.financePlatform.service.LiveInformationService;
import com.shxt.financePlatform.service.OssLiveService;
import com.shxt.financePlatform.service.SubjectSectionService;
import com.shxt.financePlatform.utils.AuthenticationUtils;
import com.shxt.financePlatform.utils.DateUtils;
import com.shxt.financePlatform.utils.RandomUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zt
 * @create 2023-10-29 20:48
 */
@RestController
public class LiveController {

    @Resource
    private OssLiveService ossLiveService;

    @Resource
    private LiveInformationService liveInformationService;

    @Resource
    private SubjectSectionService sectionService;

    @Resource
    private ClientCourseVoService clientCourseVoService;

    /**
     * 监测判断直播是否连接
     * @param url 请求地址
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @GetMapping("/judgeLiveStatus")
    public R<Boolean> judgeLiveStatus(@RequestParam String url){
        if (ossLiveService.JudgeLiving(url))
            return R.success(true,null);
        else
            return R.success(false,null);
    }

    /**
     * 生成并返回推流信息
     * @param courseId 课程id
     * @param subjectId 专题id
     * @param sectionId 小节id
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @GetMapping("/liveUrl")
    public R<Map<String,Object>> getLiveUrl(@RequestParam String liveTitle,@RequestParam Integer courseId,
                                            @RequestParam Integer subjectId, @RequestParam Integer sectionId){
        SubjectSection section = sectionService.getById(sectionId);
        //距离小节开始时间小于10分钟且没到小节开始时间允许获取推流信息
        if (DateUtils.getDiffNowMinutes(section.getStartTime()) < 0)
            return R.err("已过开播时间");
        if (DateUtils.getDiffNowMinutes(section.getStartTime()) > 10){
            return R.err("距离小节开播时间大于10分钟,请稍后重试");
        }
        //教师id
        Integer teacherId = AuthenticationUtils.getUserId();
        //检测是否有直播缓存信息
        LiveCache liveCache = ossLiveService.getTeacherLiveCache(teacherId);
        if (liveCache != null)
            return R.err("系统错误");
        // 生成LiveChannel名称。
        String uuid = RandomUtils.getUUID(4);
        String liveChannelName = "course" + courseId;
        //使用课程主题、主题小节id命名playlistName,防止重复
        String playlistName = "subject" + subjectId + "_section" + sectionId + "_courseLive" + uuid + ".m3u8";
        String playBackName = "subject" + subjectId + "_section" + sectionId + "_playBack" + uuid + ".m3u8";
        Map<String, Object> map = ossLiveService.getLiveMessage(liveChannelName, playlistName);
        String signRtmpUrl = (String) map.get("signRtmpUrl");
        //从signRtmpUrl分离出推流服务器地址和推流吗
        //推流服务器地址
        String serverUrl = signRtmpUrl.substring(0,signRtmpUrl.indexOf("/live")+5);
        //推流码
        String rtmpCode = signRtmpUrl.substring(signRtmpUrl.indexOf("/live")+6);
        //失效时间戳
        long disabledTime = (Long) map.get("disabledTime");
        //播放地址
        String playUrl = (String)map.get("playUrl");
        //创建缓存
        liveCache = new LiveCache(null,disabledTime, playUrl, rtmpCode, serverUrl, teacherId, playBackName,
                courseId, subjectId, sectionId,liveTitle, liveChannelName, "等待开播");
        ossLiveService.saveLiveCache(liveCache);   //存储直播缓存信息
        //解析返回
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("serverUrl",serverUrl);
        hashMap.put("rtmpCode",rtmpCode);
        hashMap.put("playUrl",playUrl);
        return R.success(hashMap,null);
    }


    /**
     * 获取直播状态
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @GetMapping("/liveCache")
    public R<LiveCache> getLiveCache(){
        Integer teacherId = AuthenticationUtils.getUserId();
        LiveCache liveCache = ossLiveService.getTeacherLiveCache(teacherId);
        if (liveCache == null){
            LiveCache cache = new LiveCache();
            cache.setStatus("无状态");
            return R.success(cache,null);
        }
        return R.success(liveCache,null);
    }


    /**
     * 开始直播
     * @return 开始结果
     */
    @PreAuthorize("hasRole('teacher')")
    @PostMapping("/startLive")
    public R<String> startLive(){
        Integer teacherId = AuthenticationUtils.getUserId();
        LiveCache liveCache = ossLiveService.getTeacherLiveCache(teacherId);
//        System.out.println(liveCache);
        //检查用户是否获取过推流码
        if (liveCache == null)
            return R.err("请重新获取推流信息");
        //检查直播是否已被连接
        else if (!ossLiveService.JudgeLiving(liveCache.getPlayUrl()))
            return R.err("直播未连接,请检测本地连接");
        //保存开播记录信息
        LiveInformation liveInformation = new LiveInformation(null, liveCache.getLiveChannelName(), liveCache.getServerUrl(),
                liveCache.getRtmpCode(), liveCache.getPlayBackName(), System.currentTimeMillis()/1000, null,
                liveCache.getDisabledTime(), new Date(), liveCache.getPlayUrl(), liveCache.getTeacherId(),
                liveCache.getLiveTitle(),liveCache.getCourseId(),liveCache.getSubjectId(),liveCache.getSectionId());
        liveInformationService.save(liveInformation);
        //设置缓存对应的直播记录id
        liveCache.setLiveId(liveInformation.getLiveId());
        //更改缓存信息
        ossLiveService.changeLiveCacheStatus(liveCache);
        return R.success(null,"开始直播成功");
    }


    /**
     * 清除直播信息缓存
     */
    @PreAuthorize("hasRole('teacher')")
    @DeleteMapping("/cleanLiveCache")
    public R<String> cleanLiveCache(){
        //获取缓存信息
        Integer teacherId = AuthenticationUtils.getUserId();
        LiveCache teacherLiveCache = ossLiveService.getTeacherLiveCache(teacherId);
        if (teacherLiveCache != null){                  //有缓存清除缓存
            if (teacherLiveCache.getStatus().equals("正在直播"))
                return R.err("正在直播中,无法重新选择");
            ossLiveService.deleteLiveCache(teacherId);
        }
        return R.success(null,"清除成功");
    }


    /**
     * 结束直播
     * @return
     */
    @PreAuthorize("hasRole('teacher')")
    @DeleteMapping("/endLive")
    public R<String> endLive(){
        //教师id
        Integer teacherId = AuthenticationUtils.getUserId();
        LiveCache liveCache = ossLiveService.getTeacherLiveCache(teacherId);
        if (liveCache == null)
            return R.err("系统异常,未找到直播信息");
        else{
            Integer liveId = liveCache.getLiveId();
            if (liveId == null){
                return R.err("直播未开始");
            }else {
                //结束直播记录直播信息
                LiveInformation li = liveInformationService.getById(liveId);
                li.setEndTime(System.currentTimeMillis()/1000);
                //保存回放获取回放地址
                try {
                    String s = ossLiveService.savePlayBack(li.getChannelName(),li.getPlayBackName(),
                            li.getStartTime(), li.getEndTime());
                    SubjectSection section = new SubjectSection(li.getSectionId());
                    section.setVideoUrl(s);
                    sectionService.updateById(section);            //更新小节回放链接
                    return R.success(null,"结束直播成功,回放将在稍后生成");
                } catch (Exception e) {             //直播了但没有数据
                    return R.success(null,"结束直播成功,回放生成失败,请联系管理员");
                } finally {
                    liveInformationService.updateById(li);         //更新直播信息
                    ossLiveService.deleteLiveCache(teacherId);     //删除缓存
                }
            }
        }
    }


    /**
     * 获取用户正在直播的报名课程
     * @return
     */
    @PreAuthorize("hasRole('client')")
    @GetMapping("/clientLivingCourse")
    public R<List<LiveInformation>> getClientLivingCourse(){
        Integer clientId = AuthenticationUtils.getUserId();
        //获取正在直播的直播信息(live_id、live_date、course_id)
        List<LiveInformation> clientLivingCourse = liveInformationService.getClientLivingCourse(clientId);
        //填充直播的课程信息(课程封面url和课程名字、教师名字)
        for (LiveInformation li: clientLivingCourse){
            li.setCourseInfo(clientCourseVoService.getClientCourse(li.getCourseId()));
        }
        return R.success(clientLivingCourse,null);
    }


    /**
     * 获取直播信息
     * @param liveId 直播信息id
     * @return
     */
    @GetMapping("/liveInfo")
    public R<LiveInformation> getLiveInfo(Integer liveId){
        LiveInformation liveInformation = liveInformationService.getLiveInformation(liveId);
        return R.success(liveInformation,null);
    }
}