package top.watech.backmonitor.config;

import org.aspectj.lang.annotation.Before;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.repository.SrpRepository;
import top.watech.backmonitor.service.TestQuartz;

import java.util.List;


/**
 * Created by fhm on 2018/7/24.
 */

@Configuration
public class QuartzConfig {
    @Autowired
    static SrpRepository srpRepository;


    public static void main(String[] args) throws Exception {
        QuartzConfig q= new QuartzConfig();
        List<SRP> srpList = srpRepository.findAll();
        for (SRP srp : srpList) {
            if (srp.isSwitchs()) {
                q.testQuartDetail(srp);
                q.testQuartTrigger(srp);
            }
        }
    }
//
//    public SRP srpSel(){
//
//        return null;
//    }

    @Bean
    public JobDetail testQuartDetail(SRP srp) {
//
//        List<SRP> srpList = srpRepository.findAll();
//        List<Long> srpIdList=null;
//        for (SRP srp : srpList) {
//            if (srp.isSwitchs()) {
//                srpIdList.add(srp.getSrpId());
//            }
//        }
//        String s = srpIdList.toString();
//        System.err.println(s);
        JobDetail jobDetail = JobBuilder
                        .newJob(TestQuartz.class)
                        .withIdentity("testQuart")//.withIdentity("srpId")

                        .usingJobData("srpId", srp.getSrpId())
//                        .usingJobData("freq", srp.getFreq())
                        .storeDurably()
                        .build();
                return jobDetail;

//        return null;

    }

    @Bean
    public Trigger testQuartTrigger(SRP srp) {
//        SRP srp = srpRepository.findBySrpIdOrderBySrpId(66L);
        int freq = (int) srp.getFreq();
        //设置开始时间为1分钟后
//        long startAtTime = System.currentTimeMillis() + 1000 * 20;
//        SimpleScheduleBuilder simpleScheduleBuilder
//                = SimpleScheduleBuilder
//                .simpleSchedule()
//                .withIntervalInMinutes(freq)
//                .repeatForever();
        return TriggerBuilder
                .newTrigger()
                .forJob(testQuartDetail(srp))
                .withIdentity("testQuart")      //.startAt(new Date(startAtTime))
//                .withSchedule(simpleScheduleBuilder) //这个可以换cron表达式的形式
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(freq)
                        .withRepeatCount(0)
                )
                .build();
    }

//    /*对Scheduer进行重新配置设置Scheduler的JobFactory使用我们自己创建的JobFactory*/
//    @Autowired
//    private JobFactory jobFactory;
//
//    @Bean(name = "schedulerFactoryBean")
//    public SchedulerFactoryBean createSchedulerFactoryBean(){
//        SchedulerFactoryBean schedulerFactoryBean=new SchedulerFactoryBean();
//        schedulerFactoryBean.setOverwriteExistingJobs(true);
//        schedulerFactoryBean.setJobFactory(jobFactory);
//        return schedulerFactoryBean;
//    }
//
//    @Bean
//    public JobDetailImpl createJobDetailsImpl(){
//        return new JobDetailImpl();
//    }

}
