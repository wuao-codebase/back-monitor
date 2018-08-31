package top.watech.backmonitor.config;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import top.watech.backmonitor.util.MyJobFactory;

/**
 * Created by fhm on 2018/7/30.
 *
 * @Description:定时任务创建JobFactory
 */
@Configuration
public class QuartzConfigration {
    @Autowired
    private MyJobFactory myJobFactory;  //自定义的factory

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        /**
          * @Description:  获取工厂bean
          * @param:
          * @return:
         */
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(myJobFactory);
        return schedulerFactoryBean;
    }

    @Bean(name = "scheduler")
    public Scheduler scheduler() {
        /**
          * @Description:  创建schedule
          * @param:
          * @return:
         */
        return schedulerFactoryBean().getScheduler();
    }
}

