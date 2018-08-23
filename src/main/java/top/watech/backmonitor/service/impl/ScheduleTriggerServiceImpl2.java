package top.watech.backmonitor.service.impl;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.repository.SrpRepository;
import top.watech.backmonitor.service.MyTask1;
import top.watech.backmonitor.service.ScheduleTriggerService;

import javax.annotation.PostConstruct;
import java.util.List;


@Service

public class ScheduleTriggerServiceImpl2 implements ScheduleTriggerService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleTriggerServiceImpl2.class);

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private SrpRepository srpRepository;

    @Override
    @Scheduled(cron = "0 */3 * * * ?")  //"0/50 * * * * ?"0 0 23:00 * * ?每天晚上11点调用这个方法来更新quartz中的任务
    @PostConstruct
    public void refreshTrigger() {
        try {
            //查询出数据库中所有的定时任务
            List<SRP> srpList = srpRepository.findAll();
            if (srpList != null) {
                for (SRP srp : srpList) {
//                    synchronized (srp) {
                        boolean switchs = srp.isSwitchs();//该任务触发器目前的状态
                        String srpId = String.valueOf(srp.getSrpId());
                        TriggerKey triggerKey = TriggerKey.triggerKey(srpId);
                        SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);
                        //说明本条任务还没有添加到quartz中
                        if (null == trigger) {
                            if (!switchs) { //如果是禁用，则不用创建触发器
                                continue;
                            }
                            JobDetail jobDetail = null;
                            jobDetail = JobBuilder.newJob((Class<? extends Job>) MyTask1.class)
                                    .withIdentity(srpId).build();
                            //执行频率
                            int freq = (int) srp.getFreq();
                            SimpleScheduleBuilder simpleScheduleBuilder
                                    = SimpleScheduleBuilder
                                    .simpleSchedule()
                                    .withIntervalInMinutes(freq)
//                                    .withIntervalInSeconds(freq)
                                    .repeatForever();
                            trigger = TriggerBuilder.newTrigger()
                                    .withIdentity(srpId)
                                    .withSchedule(simpleScheduleBuilder)//这个可以换cron表达式的形式
                                    .build();
                            //把trigger和jobDetail注入到调度器
                            synchronized (srp){
                                scheduler.scheduleJob(jobDetail, trigger);
                            }
                        } else {  //说明查出来的这条任务，已经设置到quartz中了
                            // Trigger已存在，先判断是否需要删除，如果不需要，再判定是否时间有变化
                            if (!switchs) { //如果是禁用，从quartz中删除这条任务
                                JobKey jobKey = JobKey.jobKey(srpId);
                                scheduler.deleteJob(jobKey);
                                continue;
                            }

                            double currentFreq = trigger.getRepeatInterval();
                            int freq = (int) srp.getFreq(); //获取数据库的
                            if (freq != currentFreq) {  //说明该任务有变化，需要更新quartz中的对应的记录
                                //按新的freq重新构建trigger
                                SimpleScheduleBuilder simpleScheduleBuilder
                                        = SimpleScheduleBuilder
                                        .simpleSchedule()
                                       .withIntervalInMinutes(freq)
//                                        .withIntervalInSeconds(freq)
                                        .repeatForever();
                                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                                        .withSchedule(simpleScheduleBuilder).build();
                                //按新的trigger重新设置job执行
                                scheduler.rescheduleJob(triggerKey, trigger);
                            }
                        }
                    }
                }
//            }
        } catch (Exception e) {
            logger.error("定时任务每日刷新触发器任务异常，在ScheduleTriggerServiceImpl的方法refreshTrigger中，异常信息：", e);
        }
    }
}
