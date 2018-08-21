package top.watech.backmonitor.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import top.watech.backmonitor.entity.DetailReport;
import top.watech.backmonitor.entity.MonitorItem;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.entity.TotalReport;
import top.watech.backmonitor.repository.DetailReportRepository;
import top.watech.backmonitor.repository.SrpRepository;
import top.watech.backmonitor.repository.TotalReportRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by fhm on 2018/7/26.
 * 1、按classify取SRP所有监控项，getMonitTtemListBySrpId
 * 2、监控流程
 *      ①SSO登录监控。POST，取参数，RestTemplete，判断连接，取状态码判断是否200，
 *      取tooken判断是否为null，是就往下继续走判断接口，否就返回
 *      ②SSO下接口
 *      ③SRP登录
 *      ④SRP下接口登录
 * 3、监控项结果写入数据库（详细监控报告 + 总监控报告）
 * 4、出现异常，微信推送
 */
@Service
@Slf4j
public class MonitorService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    MonitorItemService monitorItemService;
    @Autowired
    FanyaDevService fanyaDevService;
    @Autowired
    WeixinSendService weixinSendService;
    @Autowired
    SrpRepository srpRepository;
    @Autowired
    DetailReportRepository detailReportRepository;
    @Autowired
    TotalReportRepository totalReportRepository;

    //tooken
    public String token;

    //accessToken
    public String accessToken;

    //成功总数
    public int sucCount = 0;

    //监控项结果
    boolean code = true;   //0为失败，1为成功
    String errMsg;  //工作正常  or  断言  (监控日志里用)
    String msgBody; //接口返回体  or  e.getMessage

    //处理接口类型（RestTemplete）
    //取结果与断言比对，最后结果为code，成功还是失败
    public boolean apiMonitor(MonitorItem monitorItem) {
        String url = monitorItem.getUrl();
        Integer requestType = monitorItem.getRequestType();
        String asserts = monitorItem.getAsserts();

        code = true;   //0为失败，1为成功

        HttpHeaders requestHeaders = new HttpHeaders();
        String requestBodyStr = monitorItem.getRequestBody();
        Map requestBody = new HashMap();
        ResponseEntity<String> responseEntity = null;

        //POST型，登录生成token
        if (requestType == 1) {
            requestBody = (Map) JSON.parse(requestBodyStr);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                msgBody = responseEntity.getBody();//取返回体
            } catch (Exception e) {
                if (monitorItem.getClassify() == 1) {
                    String str = monitorItem.getMonitorName() + "接口，返回异常，返回信息：" + e.getMessage();
                    System.err.println(str);
//                    System.err.println("错误信息：" + e.getMessage());
                    errMsg = e.getMessage();
                    msgBody = e.getMessage();//返回信息
                    code = false;
                    log.error("监控SSO登录接口异常，在MonitorService的apiMonitor方法中，异常信息：",e);
                    return Boolean.parseBoolean(null);
                } else if (monitorItem.getClassify() == 3){
                    String str = "SRP登录出错！";
                    System.err.println(str);
                    System.err.println("错误信息：" + e.getMessage());
                    errMsg = str + e.getMessage();
                    msgBody = e.getMessage();//返回信息
                    code = false;
                    log.error("监控SRP登录接口异常，在MonitorService的apiMonitor方法中，异常信息：",e);
                    return Boolean.parseBoolean(null);
                }
                else {
                    String str = monitorItem.getMonitorName() + "接口，返回异常，返回信息：" + e.getMessage();
                    System.err.println(str);
                    errMsg = e.getMessage();
                    msgBody = e.getMessage();//返回信息
                    code = false;
                    log.error("监控post类型接口异常，在MonitorService的apiMonitor方法中，异常信息：",e);
                }
            }
            JSONObject resJsonObject = JSON.parseObject(responseEntity.getBody());//接口返回内容的json对象
            JSONArray assertsArraysJsonObject = JSON.parseArray(asserts);//传来的断言的json对象数组

            //断言数组循环判断,与监控项返回的JSON对象比对
            for (Object assJsonObject : assertsArraysJsonObject) {
                JSONObject assJsonObject1 = (JSONObject) assJsonObject;

                //ststus为0是等号，为1是不等号
                if (assJsonObject1.get("ststus").equals("0")) {
                    if (resJsonObject.get(assJsonObject1.get("akey")).equals(assJsonObject1.get("value"))) {
                        code = (code & true);//成功
                    } else {
                        code = (code & false);//失败
                        errMsg = assJsonObject1.get("akey") + " = " + resJsonObject.get(assJsonObject1.get("akey")) ;//断言，e.g. connect=false

                    }
                } else {
                    if (!resJsonObject.get(assJsonObject1.get("akey")).equals(assJsonObject1.get("value")))
                        code = (code & true);    //所有断言判断都成功这个监控项才算成功，有一个失败就算失败
                    else {
                        code &= false;
                        errMsg = assJsonObject1.get("akey") + " = null" ;//断言，e.g. token=null
                    }
                    if (monitorItem.getClassify() == 1) {
                        accessToken = (String) resJsonObject.get(assJsonObject1.get("akey"));
                    } else if (monitorItem.getClassify() == 3) {
                        token = (String) resJsonObject.get(assJsonObject1.get("akey"));
                    }
                }
            }
        }
        //GET请求，携带token访问
        else if (requestType == 2) {
            if (monitorItem.getClassify() == 2) {
                if (accessToken == null){
                    return Boolean.parseBoolean(null);
                }
                requestHeaders.add("Authorization", "Bearer " + accessToken);
            } else if (monitorItem.getClassify() == 4) {
                if (token == null){
                    return Boolean.parseBoolean(null);
                }
                requestHeaders.add("Authorization", "Bearer " + token);
            }

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

                msgBody = responseEntity.getBody();//取返回体

                JSONObject resJsonObject = JSON.parseObject(responseEntity.getBody());//接口返回内容的json对象
                JSONArray assertsArraysJsonObject = JSON.parseArray(asserts);//传来的断言的json对象数组

                //断言数组循环判断
                for (Object assJsonObject : assertsArraysJsonObject) {
                    JSONObject assJsonObject1 = (JSONObject) assJsonObject;
                    if (assJsonObject1.get("ststus").equals("0")) {
                        //接口返回的对应asserts中value的值
                        String revalue = resJsonObject.get(assJsonObject1.get("akey")).toString();
                        if (revalue.equals(assJsonObject1.get("value")))
                            code = (code & true);//成功
                        else {
                            code = (code & false);//失败
                            errMsg = assJsonObject1.get("akey") + " = " + revalue ;//断言，e.g. connect=false
                        }
                    } else {
                        if (!resJsonObject.get(assJsonObject1.get("akey")).equals(assJsonObject1.get("value")))
                            code = (code & true);    //所有断言判断都成功这个监控项才算成功，有一个失败就算失败
                        else {
                            code = (code & false);
                            errMsg = assJsonObject1.get("akey") + " = null" ;//断言，e.g. token=null
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("错误信息：" + e.getMessage());
                errMsg = e.getMessage();
                msgBody = e.getMessage();//返回信息
                code = false;
                log.error("监控API类型接口异常，在MonitorService的apiMonitor方法中，异常信息：",e);
            }
        }
        if (code == true) {
            System.err.println(monitorItem.getMonitorName() + ":" + "工作正常");
            System.err.println("*******************************************");
            errMsg = "";
            sucCount = sucCount + 1;
        } else {
            System.err.println(monitorItem.getMonitorName() + ":" + "工作异常");
            try {
                System.err.println("错误信息：" + responseEntity.getBody());
//                errMsg = responseEntity.getBody();
            } catch (Exception e) {
                System.err.print("");
                log.error("监控API类型接口异常，在MonitorService的apiMonitor方法中，异常信息：",e);
            }


            System.err.println("*******************************************");
        }
        return code;
    }

    //处理视频类型，最后结果是code，成功还是失败
    public void videoMonitor(MonitorItem monitorItem) {
        //1、测试视频相关所有接口
        //2、测试视频文件获取
        String domain =  monitorItem.getUrl();
        JSONObject params = (JSONObject) JSONObject.parse( monitorItem.getRequestBody());
        String ivsid= params.getString("ivsid");
        String channel= params.getString("channel");
        VideoMmonit videoMmonit = new VideoMmonit();
        DetailReport monite = videoMmonit.monite(domain, ivsid, channel);
        code = monite.getCode() ;
        errMsg = monite.getMessage();
        msgBody =monite.getMessageBody();
        if (code == true) {
            sucCount = sucCount + 1;
        }
    }

    //处理页面类型，最后结果是code，成功还是失败 throws RestClientException
    public void pageMonitor(MonitorItem monitorItem) {
        code = true;   //0为失败，1为成功

        String url = monitorItem.getUrl();
        HttpHeaders requestHeaders = new HttpHeaders();
        Map requestBody = new HashMap();
        if (monitorItem.getClassify() == 2) {
            requestHeaders.add("Authorization", "Bearer " + accessToken);
        } else if (monitorItem.getClassify() == 4) {
            requestHeaders.add("Authorization", "Bearer " + token);
        }

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            code = true;//成功
            System.err.println(monitorItem.getMonitorName() + ":" + "工作正常");
            System.err.println("*******************************************");
            errMsg = "";

            msgBody = responseEntity.getBody();//取返回体

            sucCount = sucCount + 1;
        } catch (Exception e) {
            code = false;//失败
            System.err.println(monitorItem.getMonitorName() + ":" + "工作异常");
            System.err.println("错误信息：" + e.getMessage());
            errMsg = e.getMessage();

            msgBody = e.getMessage();//返回信息
            log.error("监控前端页面类型接口异常，在MonitorService的pageMonitor方法中，异常信息：",e);

            System.err.println("*******************************************");
        }
    }

    /**
     * SRP监控流程
     * 1、SSO登录。2、平台接口。3、SRP登录。4、SRP下接口。
     */
    @Transactional
    public void monitorLogic(Long srpId) {

        synchronized (this) {
            //总的监控报告
            TotalReport totalReport = new TotalReport();
            totalReportRepository.save(totalReport);

            //格式化时间
            SimpleDateFormat str =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //开始时间
            Date start = new Date();
            String startTime1 = str.format(start);
            Date startTime = null;
            try {
                startTime = str.parse(startTime1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.err.println("开始时间:" + startTime);

            //SRP的所有监控项（sort by classify）
            List<MonitorItem> monitorItems = monitorItemService.getMonitTtemListBySrpId(srpId);
            System.err.println("start!");
            for (MonitorItem monitorItem : monitorItems) {
                Integer classify = monitorItem.getClassify();
                //平台登录
                if (monitorItem.getClassify() == 1) {
                    apiMonitor(monitorItem);

                    //详细监控报告
                    DetailReport detailReport = new DetailReport();
                    detailReport.setCode(code);
                    detailReport.setMessage(errMsg);
                    detailReport.setMessageBody(msgBody);
                    detailReport.setMonitorId(monitorItem.getMonitorId());
                    detailReport.setMonitorName(monitorItem.getMonitorName());
                    detailReport.setTotalReport(totalReport);
                    detailReportRepository.save(detailReport);
                }
                //平台接口
                else if (monitorItem.getClassify() == 2 && accessToken != null) {
                    //视频类型的监控项
                    if (monitorItem.getMonitorType() == 2) {
                        videoMonitor(monitorItem);
                    }
                    //接口类型
                    else if (monitorItem.getMonitorType() == 1) {
                        apiMonitor(monitorItem);
                    }
                    //页面监控
                    else {
                        pageMonitor(monitorItem);
                    }
                    //详细监控报告
                    DetailReport detailReport = new DetailReport();
                    detailReport.setCode(code);
                    detailReport.setMessage(errMsg);
                    detailReport.setMessageBody(msgBody);
                    detailReport.setMonitorId(monitorItem.getMonitorId());
                    detailReport.setMonitorName(monitorItem.getMonitorName());
                    detailReport.setTotalReport(totalReport);
                    detailReportRepository.save(detailReport);
                }

                //SRP登录
                if (monitorItem.getClassify() == 3) {
                    apiMonitor(monitorItem);
                    //详细监控报告
                    DetailReport detailReport = new DetailReport();
                    detailReport.setCode(code);
                    detailReport.setMessage(errMsg);
                    detailReport.setMessageBody(msgBody);
                    detailReport.setMonitorId(monitorItem.getMonitorId());
                    detailReport.setMonitorName(monitorItem.getMonitorName());
                    detailReport.setTotalReport(totalReport);
                    detailReportRepository.save(detailReport);
                }
                //SRP接口
                    else if (monitorItem.getClassify() == 4 && token != null) {
                    //视频类型的监控项
                    if (monitorItem.getMonitorType() == 2) {
                        videoMonitor(monitorItem);
                    }
                    //接口类型
                    else if (monitorItem.getMonitorType() == 1) {
                        apiMonitor(monitorItem);
                    }
                    //页面监控
                    else {
                        pageMonitor(monitorItem);
                    }
                    //详细监控报告
                    DetailReport detailReport = new DetailReport();
                    detailReport.setCode(code);
                    detailReport.setMessage(errMsg);
                    detailReport.setMessageBody(msgBody);
                    detailReport.setMonitorId(monitorItem.getMonitorId());
                    detailReport.setMonitorName(monitorItem.getMonitorName());
                    detailReport.setTotalReport(totalReport);
                    detailReportRepository.save(detailReport);
                }
                else if (monitorItem.getClassify() == 5){
                    continue;
                }
            }
            System.err.println("监控项总个数：" + monitorItems.size());
            System.err.println("监控项成功个数：" + sucCount);
            System.err.println("*******************************************");

            if (srpId == 66L && token!=null) {
                fanyaDevService.testDev();
                //详细监控报告
                DetailReport detailReport = new DetailReport();
                detailReport.setCode(FanyaDevService.totalCode);
                detailReport.setMessage(FanyaDevService.devMsg);
                detailReport.setMessageBody(String.valueOf(FanyaDevService.msgBody));
                detailReport.setMonitorId(42L);
                detailReport.setMonitorName("设备信息获取");
                detailReport.setTotalReport(totalReport);
                detailReportRepository.save(detailReport);
                FanyaDevService.devMsg = "";
                if (FanyaDevService.totalCode){
                    sucCount = sucCount + 1 ;
                }
            }

            //结束时间
            Date end = new Date();
            String endTime1 = str.format(end);
            Date endTime = null;
            try {
                endTime = str.parse(endTime1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.err.println("结束时间:" + endTime);

            //总的监控报告
            totalReport.setStartTime(startTime);
            totalReport.setEndTime(endTime);
            totalReport.setMonitorNum(monitorItems.size());
            SRP srp = srpRepository.findBySrpId(srpId);
            totalReport.setSrp(srp);
            int errorCount = monitorItems.size() - sucCount;
            totalReport.setErrorCount(errorCount);
            totalReportRepository.saveAndFlush(totalReport);

            /**
             * 微信推送，出错才推
             */
            if (totalReport.getErrorCount() > 0){
                weixinSendService.weixinSend(totalReport);
                WeixinSendService.weixinErrmsg = "";
                WeixinSendService.errorNotice = "";
            }
            sucCount = 0;
        }
    }

}
