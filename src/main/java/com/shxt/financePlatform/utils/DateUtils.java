package com.shxt.financePlatform.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zt
 * @create 2023-10-28 16:47
 */
public class DateUtils {

    //获取指定日期的年月日
    public static String getDateTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }

    /**
     * 获取n天后的date日期
     * @param n 天数
     * @param startDate 开始时间
     * @return
     */
    public static Date getNDaysDate(Integer n,Date startDate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        // 增加n天
        calendar.add(Calendar.DAY_OF_YEAR, n);

        // 获取增加n天后的日期
        return calendar.getTime();
    }

    /**
     * 获取某日期距离现在的分钟差
     * @param date
     * @return
     */
    public static long getDiffNowMinutes(Date date){
        long timestamp1 = date.getTime(); // 获取Date对象的时间戳
        long timestamp2 = System.currentTimeMillis(); // 获取当前时间戳

        long timeDiff = timestamp1 - timestamp2; // 计算时间差（单位：毫秒）

        long minutes = timeDiff / 60000; // 将时间差转换为分钟
        return minutes;
    }
}
