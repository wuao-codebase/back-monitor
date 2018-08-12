//package top.watech.backmonitor.service;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import org.quartz.JobDataMap;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.quartz.JobKey;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.quartz.QuartzJobBean;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//import top.watech.backmonitor.entity.MonitorItem;
//import top.watech.backmonitor.repository.MonitorItemRepository;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by fhm on 2018/7/24.
// * 执行定时任务
// */
//
//public class TestQuartz extends QuartzJobBean{//
//
//    @Autowired
//    MonitorService monitorService;
//
//    @Override
//    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//
//        JobKey key = jobExecutionContext.getJobDetail().getKey();
//        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
//        Long srpId = dataMap.getLong("srpId");
////        double freq = dataMap.getDouble("freq");
//
//        System.err.println("key : " + key);
//        System.err.println("srpId : " + srpId);
////        System.err.println("freq : " + freq);
//
//        //写监控逻辑
//        System.out.println("quart task-当前时间："+new Date());
//        monitorService.monitorLogic(srpId);
//    }
//
//}
