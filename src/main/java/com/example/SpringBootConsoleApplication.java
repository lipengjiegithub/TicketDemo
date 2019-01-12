package com.example;

import com.example.demo.DemoJob;
import com.example.demo.Verify;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;

import java.util.Scanner;


public class SpringBootConsoleApplication {

    public static void main(String[] args) throws Exception {

        new SpringBootConsoleApplication().start();

    }

    public void start() throws Exception {
        boolean flag = Rob.login();
        if(!flag) return;

        Rob.query();

        Rob.submit();


        //创建scheduler
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        startVerifyTask(scheduler);

        startQueryOrderTask(scheduler);

        //启动之
        scheduler.start();
    }

    public static void startVerifyTask(Scheduler scheduler) throws Exception{
        //定义一个JobDetail
        JobDetail job = JobBuilder
                .newJob()
                .ofType(Verify.class)
                .storeDurably(true)
                .build();

        //定义一个Trigger
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .forJob(job)
                .withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?"))
                .build();


        //加入这个调度
        scheduler.scheduleJob(job, trigger);
    }

    public static void startQueryOrderTask(Scheduler scheduler) throws Exception{
        //定义一个JobDetail
        JobDetail job = JobBuilder
                .newJob()
                .ofType(DemoJob.class)
                .storeDurably(true)
                .build();

        //定义一个Trigger
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .forJob(job)
                .withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?"))
                .build();


        //加入这个调度
        scheduler.scheduleJob(job, trigger);
    }
}
