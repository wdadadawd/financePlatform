package com.shxt.financePlatform.config;

import com.shxt.financePlatform.quartz.CourseReminderJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zt
 * @create 2023-11-10 21:15
 * Quartz配置类
 */
@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail courseReminderJobDetail() {
        return JobBuilder.newJob(CourseReminderJob.class)
                .withIdentity("courseReminderJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger courseReminderTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(courseReminderJobDetail())
                .withIdentity("courseReminderTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 */1 * ? * *")) // 每隔1分钟执行一次
                .build();
    }
}
