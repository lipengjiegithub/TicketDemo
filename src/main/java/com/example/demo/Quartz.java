package com.example.demo;

import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
public class Quartz {

    @Bean
    public JobDetail createJobDetail() {
        return JobBuilder
                .newJob()
                .ofType(DemoJob.class)
                .storeDurably(true)
                .withIdentity("job1", "jobGroup1")
                .build();
    }

    @Bean
    public Trigger createTrigger() throws Exception{
        return TriggerBuilder
                .newTrigger()
                .forJob(createJobDetail())
                .withIdentity("trigger1", "trigger1Group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("* * 12 * * ?"))
                .build();
    }

    @Bean
    public Trigger createTrigger1() throws Exception{
        return TriggerBuilder
                .newTrigger()
                .forJob(createJobDetail())
                .withIdentity("trigger2", "trigger1Group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("* * 12 * * ?"))
                .build();
    }

}
