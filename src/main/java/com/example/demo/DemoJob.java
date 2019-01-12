package com.example.demo;

import com.alibaba.fastjson.JSONObject;
import com.example.login.Login;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DemoJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JSONObject obj = Login.myOrder();
        if(!obj.isEmpty()) {
            System.out.println("有订单未支付");
        }else {
            System.out.println("没有未支付的订单");
        }
    }
}
