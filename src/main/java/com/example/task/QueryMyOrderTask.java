package com.example.task;

import com.alibaba.fastjson.JSONObject;
import com.example.login.Login;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class QueryMyOrderTask implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Login.myOrder();
    }
}
