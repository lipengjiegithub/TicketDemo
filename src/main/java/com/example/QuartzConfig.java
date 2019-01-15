package com.example;

import com.example.task.*;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    public QuartzConfig() {
        System.out.println("init");
    }

    @Bean("queryDetail")
    public JobDetail queryDetail() {
        return JobBuilder
                .newJob()
                .ofType(QueryTask.class)
                .storeDurably(true)
                .build();
    }

    @Bean("queryTrigger")
    public Trigger queryTrigger(JobDetail queryDetail) {
        return TriggerBuilder
                .newTrigger()
                .forJob(queryDetail)
//                .withSchedule(CronScheduleBuilder.cronSchedule("55 29 13,14 * * ?"))
                .withSchedule(CronScheduleBuilder.cronSchedule("55 59 15 * * ?"))
                .build();
    }

    @Bean("verifyDetail")
    public JobDetail verifyDetail(){
        return JobBuilder
                .newJob()
                .ofType(VerifyTask.class)
                .storeDurably(true)
                .build();
    }

    @Bean("verifyTrigger")
    public Trigger verifyTrigger(JobDetail verifyDetail) {
        return TriggerBuilder
                .newTrigger()
                .forJob(verifyDetail)
                .withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?"))
                .build();
    }

    @Bean("submitDetail")
    public JobDetail submitDetail(){
        return JobBuilder
                .newJob()
                .ofType(SubmitTask.class)
                .storeDurably(true)
                .build();
    }

    @Bean("submitTrigger")
    public Trigger submitTrigger(JobDetail submitDetail) {
        return TriggerBuilder
                .newTrigger()
                .forJob(submitDetail)
//                .withSchedule(CronScheduleBuilder.cronSchedule("55 29 13,14 * * ?"))
                .withSchedule(CronScheduleBuilder.cronSchedule("55 59 15 * * ?"))
                .build();
    }

    @Bean("myOrderDetail")
    public JobDetail myOrderDetail(){
        return JobBuilder
                .newJob()
                .ofType(QueryMyOrderTask.class)
                .storeDurably(true)
                .build();
    }

    @Bean("myOrderTrigger")
    public Trigger myOrderTrigger(JobDetail myOrderDetail) {
        return TriggerBuilder
                .newTrigger()
                .forJob(myOrderDetail)
                .withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?"))
                .build();
    }

//    @Bean("workerDetail")
//    public JobDetail workerDetail(){
//        return JobBuilder
//                .newJob()
//                .ofType(Worker.class)
//                .storeDurably(true)
//                .build();
//    }
//
//    @Bean("workerTrigger")
//    public Trigger workerTrigger(JobDetail workerDetail) {
//        return TriggerBuilder
//                .newTrigger()
//                .forJob(workerDetail)
//                .withSchedule(CronScheduleBuilder.cronSchedule("55 29 13,14 * * ?"))
//                .build();
//    }
}
