//package top.watech.backmonitor.quert;
//
///**
// * Created by fhm on 2018/8/9.
// */
//import org.quartz.Trigger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
//import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
//import org.springframework.scheduling.quartz.SchedulerFactoryBean;
//import top.watech.backmonitor.entity.SRP;
//import top.watech.backmonitor.repository.SrpRepository;
//import top.watech.backmonitor.service.ScheduleTask;
//import top.watech.backmonitor.service.TestQuartz;
//
//import java.util.List;
//
//@Configuration
//public class QuartzConfigration {
//
//    @Autowired
//    SrpRepository srpRepository;
//    List<SRP> srpList = srpRepository.findAll();
//    /**
//     * attention:
//     * Details：配置定时任务
//     */
//    @Bean(name = "jobDetail")
//    public MethodInvokingJobDetailFactoryBean detailFactoryBean(TestQuartz testQuartz) {// ScheduleTask为需要执行的任务
//        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
//        /*
//         *  是否并发执行
//         *  例如每5s执行一次任务，但是当前任务还没有执行完，就已经过了5s了，
//         *  如果此处为true，则下一个任务会执行，如果此处为false，则下一个任务会等待上一个任务执行完后，再开始执行
//         */
//        jobDetail.setConcurrent(true);
//
//        for (SRP srp : srpList){
//            if (!srp.isSwitchs()){
//                jobDetail.setName(srp.getSrpName());// 设置任务的名字
//                jobDetail.setGroup("srp");// 设置任务的分组，这些属性都可以存储在数据库中，在多任务的时候使用
//
//            }
//        }
//
//
//        /*
//         * 为需要执行的实体类对应的对象
//         */
//        jobDetail.setTargetObject(testQuartz);
//
//        /*
//         * sayHello为需要执行的方法
//         * 通过这几个配置，告诉JobDetailFactoryBean我们需要执行定时执行ScheduleTask类中的sayHello方法
//         */
//        jobDetail.setTargetMethod("monitorLogic");
//        return jobDetail;
//    }
//
//    /**
//     * attention:
//     * Details：配置定时任务的触发器，也就是什么时候触发执行定时任务
//     */
//    @Bean(name = "jobTrigger")
//    public CronTriggerFactoryBean cronJobTrigger(MethodInvokingJobDetailFactoryBean jobDetail) {
//        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
//
//        for (SRP srp : srpList) {
//            if (!srp.isSwitchs()) {
//                int freq = (int) srp.getFreq();
//                tigger.setJobDetail(jobDetail.getObject());
//                //tigger.
//                String cron = "0 0/" + freq + "* * * ?";
//                tigger.setCronExpression(cron);// 初始时的cron表达式 0 0/2 * * * ?   "0 30 20 * * ?"
//                tigger.setName(srp.getSrpName());// trigger的name
//            }
//        }
//        return tigger;
//    }
//
//    /**
//     * attention:
//     * Details：定义quartz调度工厂
//     */
//    @Bean(name = "scheduler")
//    public SchedulerFactoryBean schedulerFactory(Trigger cronJobTrigger) {
//        SchedulerFactoryBean bean = new SchedulerFactoryBean();
//        // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
//        bean.setOverwriteExistingJobs(true);
//        // 延时启动，应用启动1秒后
//        bean.setStartupDelay(1);
//        // 注册触发器
//        bean.setTriggers(cronJobTrigger);
//        return bean;
//    }
//}