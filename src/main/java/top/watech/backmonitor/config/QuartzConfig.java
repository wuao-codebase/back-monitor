package top.watech.backmonitor.config;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.repository.SrpRepository;
import top.watech.backmonitor.service.TestQuartz;


/**
 * Created by fhm on 2018/7/24.
 */

@Configuration
public class QuartzConfig {
    @Autowired
    SrpRepository srpRepository;

    @Bean
    public JobDetail testQuartDetail(){
        return JobBuilder
                .newJob(TestQuartz.class)
                .withIdentity("testQuart")
                .storeDurably().build();
    }


    @Bean
    public Trigger testQuartTrigger(){
        SRP srp = srpRepository.findBySrpIdOrderBySrpId(66L);
        int freq = (int) srp.getFreq();
        //设置开始时间为1分钟后
//        long startAtTime = System.currentTimeMillis() + 1000 * 20;
        SimpleScheduleBuilder simpleScheduleBuilder
                = SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInMinutes(freq)
                .repeatForever();
        return TriggerBuilder
                .newTrigger()
                .forJob(testQuartDetail())
                .withIdentity("testQuart")      //.startAt(new Date(startAtTime))
                .withSchedule(simpleScheduleBuilder)
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
