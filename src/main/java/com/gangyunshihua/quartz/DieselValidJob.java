package com.gangyunshihua.quartz;

import com.gangyunshihua.service.DieselService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class DieselValidJob implements Job {

    @Autowired
    private DieselService dieselService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            dieselService.flushValid();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
