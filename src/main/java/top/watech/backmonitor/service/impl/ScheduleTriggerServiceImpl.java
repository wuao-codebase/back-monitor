package top.watech.backmonitor.service.impl;

import lombok.extern.slf4j.Slf4j;
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

/**
 * 定点触发定时任务（即更新cron表达式而不是更新频率值）
 */

@Service
@Slf4j
public class ScheduleTriggerServiceImpl implements ScheduleTriggerService {

//    private static final Logger logger = LoggerFactory.getLogger(ScheduleTriggerServiceImpl.class);
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private SrpRepository srpRepository;
//    @PostConstruct
//    @Override
//    @Scheduled(cron = "0 */10 * * * ?")  //"0 05 11 * * ?"10分钟查一次数据库，调用这个方法来更新quartz中的任务
    public void refreshTrigger() {
        try {
            //查询出数据库中所有的定时任务
            List<SRP> srpList = srpRepository.findAll();
            if (srpList != null) {
                for (SRP srp : srpList) {
                    boolean switchs = srp.isSwitchs();//该任务触发器目前的状态
                    String srpId =String.valueOf(srp.getSrpId());
                    TriggerKey triggerKey = TriggerKey.triggerKey(srpId);
                    CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                    //说明本条任务还没有添加到quartz中
                    if (null == trigger) {
                        if (!switchs) { //如果是禁用，则不用创建触发器
                            continue;
                        }
                        JobDetail jobDetail = null;
                            jobDetail = JobBuilder.newJob((Class<? extends Job>) MyTask1.class)
                                    .withIdentity(srpId).build();
                            //表达式调度构建器
                            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(srp.getCron());
                            //按新的cronExpression表达式构建一个新的trigger
                            trigger = TriggerBuilder.newTrigger().withIdentity(srpId).withSchedule(scheduleBuilder).build();
                            //把trigger和jobDetail注入到调度器
                            scheduler.scheduleJob(jobDetail, trigger);
                    } else {  //说明查出来的这条任务，已经设置到quartz中了
                        // Trigger已存在，先判断是否需要删除，如果不需要，再判定是否时间有变化
                        if (!switchs) { //如果是禁用，从quartz中删除这条任务
                            JobKey jobKey = JobKey.jobKey(srpId);
                            scheduler.deleteJob(jobKey);
                            continue;
                        }
                        String searchCron =srp.getCron() ; //获取数据库的String.valueOf(srp.getFreq())
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
//            logger.error("定时任务每日刷新触发器任务异常，在ScheduleTriggerServiceImpl的方法refreshTrigger中，异常信息：", e);
            log.error("定时任务每日刷新触发器任务异常，在ScheduleTriggerServiceImpl的方法refreshTrigger中，异常信息：",e);
        }
    }
}
