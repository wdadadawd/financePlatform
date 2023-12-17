package com.shxt.financePlatform.mapper;

import com.shxt.financePlatform.entity.LiveInformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 30567
* @description 针对表【live_information】的数据库操作Mapper
* @createDate 2023-10-30 20:44:09
* @Entity com.shxt.financePlatform.entity.LiveInformation
*/
public interface LiveInformationMapper extends BaseMapper<LiveInformation> {

    /**
     * 获取已报名并正在直播的课程信息
     * @param clientId
     * @param nowTime
     * @return
     */
    @Select("SELECT li.live_id,live_date,li.course_id\n" +
            "FROM course_registration cr RIGHT JOIN live_information li\n" +
            "ON cr.course_id = li.course_id\n" +
            "WHERE cr.client_id = #{clientId}\n" +
            "AND li.end_time IS NULL\n" +
            "AND li.disabled_time > #{nowTime}")
    List<LiveInformation> getClientLivingCourse(Integer clientId,long nowTime);
}




