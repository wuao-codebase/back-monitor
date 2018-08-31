package top.watech.backmonitor.service;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时任务类
 * 里面放的是监控逻辑
 */
@Component
public class ScheduleTask implements Job {
    @Autowired
    MonitorService monitorService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            String jobName = context.getTrigger().getKey().getName();
            Long srpId = Long.valueOf(jobName);
            monitorService.monitorLogic(srpId);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}