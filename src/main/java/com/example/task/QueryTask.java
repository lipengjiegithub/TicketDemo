package com.example.task;

import com.example.Rob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class QueryTask implements Job
{
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Rob.query();
    }
}
