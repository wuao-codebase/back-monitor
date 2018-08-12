package top.watech.backmonitor.service;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
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


//    @Autowired
//    private CScheduleRecordsRepository recordsRepository;


    @Override

    public void execute(JobExecutionContext context)

            throws JobExecutionException {

        boolean isExecute = false;  //是否已执行业务逻辑

        boolean flag = false;  //业务逻辑执行后返回结果

        try {
            System.out.println("quart task-当前时间："+new Date());

            //可以通过context拿到执行当前任务的quartz中的很多信息，如当前是哪个trigger在执行该任务

            CronTrigger trigger = (CronTrigger) context.getTrigger();

            String corn = trigger.getCronExpression();
            System.err.println(corn);
            String jobName = trigger.getKey().getName();
            System.err.println(jobName);

//            String jobGroup = trigger.getKey().getGroup();
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
}