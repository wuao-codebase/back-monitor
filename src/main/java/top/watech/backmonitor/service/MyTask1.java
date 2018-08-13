package top.watech.backmonitor.service;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.watech.backmonitor.repository.SrpRepository;

import java.util.Date;

@Component
public class MyTask1 implements Job {

    //这里就可以通过spring注入bean了

    @Autowired
    SrpRepository srpRepository;
//    private CScheduleTriggerRepository jobRepository;
    @Autowired
    MonitorService monitorService;

    @Override
    public void execute(JobExecutionContext context)

            throws JobExecutionException {
        try {
            //可以通过context拿到执行当前任务的quartz中的很多信息，如当前是哪个trigger在执行该任务

//            CronTrigger trigger = (CronTrigger) context.getTrigger(); //使用cron表达式的时
            SimpleTrigger trigger = (SimpleTrigger) context.getTrigger();

            //监控逻辑
//            System.out.println("quart task-当前时间："+new Date());
            String jobName = context.getTrigger().getKey().getName();
            Long srpId = Long.valueOf(jobName);
            monitorService.monitorLogic(srpId);
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
}