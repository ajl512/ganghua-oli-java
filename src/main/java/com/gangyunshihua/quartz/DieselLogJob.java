package com.gangyunshihua.quartz;

import com.gangyunshihua.service.DieselService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class DieselLogJob implements Job {

    @Autowired
    private DieselService dieselService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            dieselService.addDieselLog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
