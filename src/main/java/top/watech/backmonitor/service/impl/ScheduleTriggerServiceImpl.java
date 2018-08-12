package top.watech.backmonitor.service.impl;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.repository.SrpRepository;
import top.watech.backmonitor.service.ScheduleTriggerService;

import java.util.List;


@Service

public class ScheduleTriggerServiceImpl implements ScheduleTriggerService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleTriggerServiceImpl.class);

    @Autowired

    private Scheduler scheduler;


    @Autowired
    private SrpRepository srpRepository;

//    private CScheduleTriggerRepository triggerRepository;


    @Override

    @Scheduled(cron = "0 0 23:00 * * ?")  //每天晚上11点调用这个方法来更新quartz中的任务

    public void refreshTrigger() {

        try {

            //查询出数据库中所有的定时任务
            List<SRP> srpList = srpRepository.findAll();
//            List<CScheduleTrigger> jobList = triggerRepository.queryAll();
            if (srpList != null) {
//            if (jobList != null) {
                for (SRP srp : srpList) {
//                for (CScheduleTrigger scheduleJob : jobList) {
                    boolean switchs = srp.isSwitchs();//该任务触发器目前的状态
//                    String status = scheduleJob.getStatus(); //该任务触发器目前的状态
                    TriggerKey triggerKey = TriggerKey.triggerKey(srp.getSrpName());
//                    TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
                    CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                    //说明本条任务还没有添加到quartz中
                    if (null == trigger) {
                        if (!switchs) { //如果是禁用，则不用创建触发器
                            continue;
                        }
                        JobDetail jobDetail = null;
                        try {
                            //创建JobDetail（数据库中job_name存的任务全路径，这里就可以动态的把任务注入到JobDetail中）
                            jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(String.valueOf(srp.getClass())))
                                    .withIdentity(srp.getSrpName()).build();
//                            jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(scheduleJob.getJobName()))
//                                    .withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup()).build();

                            //表达式调度构建器
                            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(String.valueOf(srp.getFreq()));
                            //按新的cronExpression表达式构建一个新的trigger
                            trigger = TriggerBuilder.newTrigger().withIdentity(srp.getSrpName()).withSchedule(scheduleBuilder).build();
//                            trigger = TriggerBuilder.newTrigger().withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup()).withSchedule(scheduleBuilder).build();

                            //把trigger和jobDetail注入到调度器
                            scheduler.scheduleJob(jobDetail, trigger);
                        } catch (ClassNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {  //说明查出来的这条任务，已经设置到quartz中了
                        // Trigger已存在，先判断是否需要删除，如果不需要，再判定是否时间有变化

                        if (!switchs) { //如果是禁用，从quartz中删除这条任务
                            JobKey jobKey = JobKey.jobKey(srp.getSrpName());
                            scheduler.deleteJob(jobKey);
                            continue;
                        }
                        String searchCron = String.valueOf(srp.getFreq()); //获取数据库的
//                        String searchCron = scheduleJob.getCron(); //获取数据库的
                        String currentCron = trigger.getCronExpression();
                        if (!searchCron.equals(currentCron)) {  //说明该任务有变化，需要更新quartz中的对应的记录
                            //表达式调度构建器
                            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(searchCron);
                            //按新的cronExpression表达式重新构建trigger
                            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                                    .withSchedule(scheduleBuilder).build();
                            //按新的trigger重新设置job执行
                            scheduler.rescheduleJob(triggerKey, trigger);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("定时任务每日刷新触发器任务异常，在ScheduleTriggerServiceImpl的方法refreshTrigger中，异常信息：", e);
        }
    }
}
