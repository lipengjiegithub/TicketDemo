package com.example;

import com.example.task.*;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;


public class SpringBootConsoleApplication {

    public static void main(String[] args) throws Exception {

        new SpringBootConsoleApplication().start();

    }

    public void start() throws Exception {
        boolean flag = Rob.login();
        if(!flag) return;


        //创建scheduler
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        startVerifyTask(scheduler);

        startQueryOrderTask(scheduler);

//        startRob(scheduler);

//        startQuery(scheduler);

//        startSubmit(scheduler);

        //启动之
        scheduler.start();
    }

    /**
     * 创建验证登录Task
     * @param scheduler 调度器
     * @throws Exception
     */
    public static void startVerifyTask(Scheduler scheduler) throws Exception{
        //定义一个JobDetail

        JobDetail job = JobBuilder
                .newJob()
                .ofType(VerifyTask.class)
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

    /**
     * 创建查询未支付订单的Task
     * @param scheduler 调度器
     * @throws Exception
     */
    public static void startQueryOrderTask(Scheduler scheduler) throws Exception{
        //定义一个JobDetail
        JobDetail job = JobBuilder
                .newJob()
                .ofType(QueryMyOrderTask.class)
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

    /**
     * 创建抢票的Task
     * @param scheduler 调度器
     * @throws Exception
     */
    public static void startRob(Scheduler scheduler) throws Exception{
        //定义一个JobDetail
        JobDetail job = JobBuilder
                .newJob()
                .ofType(Worker.class)
                .storeDurably(true)
                .build();

        //定义一个Trigger
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .forJob(job)
                .withSchedule(CronScheduleBuilder.cronSchedule("55 29 13,14 * * ?"))
                .build();


        //加入这个调度
        scheduler.scheduleJob(job, trigger);
    }

    /**
     * 查询任务
     * @param scheduler 调度器
     * @throws Exception
     */
    public static void startQuery(Scheduler scheduler) throws Exception{
        //定义一个JobDetail
        JobDetail job = JobBuilder
                .newJob()
                .ofType(QueryTask.class)
                .storeDurably(true)
                .build();

        //定义一个Trigger
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .forJob(job)
                .withSchedule(CronScheduleBuilder.cronSchedule("55 29 13/14 * * ?"))
                .build();


        //加入这个调度
        scheduler.scheduleJob(job, trigger);
    }

    /**
     * 提交任务
     * @param scheduler 调度器
     * @throws Exception
     */
    public static void startSubmit(Scheduler scheduler) throws Exception{
        //定义一个JobDetail
        JobDetail job = JobBuilder
                .newJob()
                .ofType(SubmitTask.class)
                .storeDurably(true)
                .build();

        //定义一个Trigger
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .forJob(job)
                .withSchedule(CronScheduleBuilder.cronSchedule("55 29 13/14 * * ?"))
                .build();


        //加入这个调度
        scheduler.scheduleJob(job, trigger);
    }
}
