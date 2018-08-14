package top.watech.backmonitor.config;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import top.watech.backmonitor.util.MyJobFactory;

@Configuration

public class QuartzConfigration {
    @Autowired

    private MyJobFactory myJobFactory;  //自定义的factory


//获取工厂bean

    @Bean

    public SchedulerFactoryBean schedulerFactoryBean() {

        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

//        try {

//            schedulerFactoryBean.setQuartzProperties(quartzProperties());

            schedulerFactoryBean.setJobFactory(myJobFactory);

//        } catch (IOException e) {
//
//            // TODO Auto-generated catch block
//
//            e.printStackTrace();
//
//        }

        return schedulerFactoryBean;

    }

//指定quartz.properties
//
//    @Bean
//
//    public Properties quartzProperties() throws IOException {
//
//        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
//
//        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
//
//        propertiesFactoryBean.afterPropertiesSet();
//
//        return propertiesFactoryBean.getObject();
//
//    }



//创建schedule
//
//    @Bean(name = "scheduler")

    public Scheduler scheduler() {

//        System.err.println(schedulerFactoryBean().getScheduler());
        return schedulerFactoryBean().getScheduler();

    }

}

