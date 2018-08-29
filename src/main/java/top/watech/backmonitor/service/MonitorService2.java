package top.watech.backmonitor.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import top.watech.backmonitor.entity.DetailReport;
import top.watech.backmonitor.entity.MonitorItem;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.entity.TotalReport;
import top.watech.backmonitor.repository.DetailReportRepository;
import top.watech.backmonitor.repository.MonitorItemRepository;
import top.watech.backmonitor.repository.SrpRepository;
import top.watech.backmonitor.repository.TotalReportRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1、取监控项
 * 2、判断监控项类型，接口、页面、视频
 * ①接口：判断classify
 * a)classify=1、3    断言、取token
 * b)classify=2、4    取requestType，exchange()
 * ②页面：
 * 取requestType，exchange()
 * ③视频
 */
@Service
@Slf4j
public class MonitorService2 {
    //    @Autowired
//    private RestTemplate restTemplate;
    @Autowired
    MonitorItemService monitorItemService;
    @Autowired
    FanyaDevService fanyaDevService;
    @Autowired
    AVideoMmonit aVideoMmonit;
    @Autowired
    WeixinSendService weixinSendService;
    @Autowired
    SrpRepository srpRepository;
    @Autowired
    DetailReportRepository detailReportRepository;
    @Autowired
    TotalReportRepository totalReportRepository;
    @Autowired
    MonitorItemRepository monitorItemRepository;

//    SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
//    RestTemplate restTemplate = null ;

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

    /**
     * SRP监控流程
     * 1、API。2、页面。3、视频
     * A、SSO登录。B、平台接口。C、SRP登录。D、SRP下接口。
     */
    @Transactional
    public void monitorLogic(Long srpId) {

        synchronized (this) {
            System.err.println("start!");
            System.err.println(srpId);
            token = null;
            accessToken = null;
            //总的监控报告
            TotalReport totalReport = new TotalReport();
//            totalReport.setSrp(srpRepository.findBySrpId(srpId));
//            totalReportRepository.save(totalReport);

            //格式化时间
            SimpleDateFormat str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
            int size = monitorItems.size();
            for (MonitorItem monitorItem : monitorItems) {
                Integer monitorType = monitorItem.getMonitorType();
                //接口
                if (monitorType == 1) {
                    if (monitorItem.getClassify() == 1 || monitorItem.getClassify() == 3) {
                        loginPro(monitorItem);
                    } else if (monitorItem.getClassify() == 2 || monitorItem.getClassify() == 4) {
                        apiAndPagePro(monitorItem);
                    } else {
                        continue;
                    }
                }
                //页面
                else if (monitorType == 3) {
                    apiAndPagePro(monitorItem);
                }
                //视频
                else {
                    videoPro(monitorItem);
                }

                //详细监控报告
                DetailReport detailReport = new DetailReport();
                detailReport.setCode(code);
                detailReport.setMessage(errMsg);
                detailReport.setMessageBody(msgBody);
                detailReport.setMonitorId(monitorItem.getMonitorId());
                detailReport.setMonitorName(monitorItem.getMonitorName());
                detailReport.setMonitorType(monitorItem.getMonitorType());
                detailReport.setTotalReport(totalReport);
                detailReportRepository.save(detailReport);
                if (code == true) {
                    sucCount = sucCount + 1;
                }
            }

            if (srpId == 65L && token != null) {
                fanyaDevService.testDev();
                //详细监控报告
                DetailReport drFyDev = new DetailReport();
                drFyDev.setCode(FanyaDevService.totalCode);
                drFyDev.setMessage(FanyaDevService.devMsg);
                drFyDev.setMessageBody(String.valueOf(FanyaDevService.msgBody));
                drFyDev.setMonitorId(42L);
                drFyDev.setMonitorName("设备信息获取");
                drFyDev.setMonitorType(1);
                drFyDev.setTotalReport(totalReport);
                detailReportRepository.save(drFyDev);
                FanyaDevService.devMsg = "";
                if (FanyaDevService.totalCode) {
                    sucCount = sucCount + 1;
                }
            }
            System.err.println("监控项总个数：" + size);
            System.err.println("监控项成功个数：" + sucCount);
            System.err.println("*******************************************");
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
            totalReport.setMonitorNum(size);
            totalReport.setSrpId(srpId);
//            SRP srp = srpRepository.findBySrpId(65L);
//            totalReport.setSrp(srpRepository.findBySrpId(65L));
            int errorCount = size - sucCount;
            totalReport.setErrorCount(errorCount);
            totalReportRepository.saveAndFlush(totalReport);

            /**
             * 微信推送，出错才推
             */
//            if (totalReport.getErrorCount() > 0) {
//                weixinSendService.weixinSend(totalReport);
//                WeixinSendService.weixinErrmsg = "";
//                WeixinSendService.errorNotice = "";
//            }
            sucCount = 0;
        }
    }

    //classify为1或3时(登录)生成token
    public Boolean loginPro(MonitorItem monitorItem) {
        String url = monitorItem.getUrl();
        String asserts = monitorItem.getAsserts();
        code = true;   //0为失败，1为成功
        HttpHeaders requestHeaders = new HttpHeaders();
        Map requestBody = new HashMap();
        ResponseEntity<String> responseEntity = null;
        requestBody = (Map) JSON.parse(monitorItem.getRequestBody());
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        String body = null;
        int statusCodeValue;
        RestTemplate restTemplate;
        //超时设置
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        if (monitorItem.getConnTimeout() != null) {
            simpleClientHttpRequestFactory.setConnectTimeout(monitorItem.getConnTimeout() * 1000);
        }
        restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            msgBody = responseEntity.getBody();//取返回体
            body = responseEntity.getBody();
            statusCodeValue = responseEntity.getStatusCodeValue();
        } catch (Exception e) {
            errMsg = "接口请求异常";
            msgBody = e.getMessage();//返回信息
            code = false;
            log.error("监控登录接口异常，在MonitorService的loginPro方法中，异常信息：", e);
            return Boolean.parseBoolean(null);
        }
        if (statusCodeValue != 200) {
            errMsg = "接口请求异常";
            code = false;
            return null;
        } else {
            if (body != null) {
                JSONObject resJsonObject = null;
                try {
                    resJsonObject = JSON.parseObject(body);//接口返回内容的json对象
                } catch (Exception e) {
                    if (monitorItem.getMonitorType() == 3) {
                        errMsg = "";
                    } else {
                        errMsg = "接口请求异常";
                    }
                    log.error("VCMInfoService的getVCMInfos()中json格式数据转换异常");
                }
                if (resJsonObject != null) {
                    if (asserts != null) {
                        //断言数组循环判断,与监控项返回的JSON对象比对
                        for (Object assJsonObject : JSON.parseArray(asserts)) {
                            JSONObject assJsonObject1 = (JSONObject) assJsonObject;
                            //ststus为0是等号，为1是不等号
                            if (assJsonObject1.get("ststus").equals("0")) {
                                if (resJsonObject.get(assJsonObject1.get("akey")).equals(assJsonObject1.get("value"))) {
                                    code = (code & true);//成功
                                    errMsg = "";
                                } else {
                                    code = (code & false);//失败
                                    errMsg = assJsonObject1.get("akey") + " = " + resJsonObject.get(assJsonObject1.get("akey"));//断言，e.g. connect=false
                                }
                            } else if (assJsonObject1.get("ststus").equals("1")) {
                                if (!resJsonObject.get(assJsonObject1.get("akey")).equals(assJsonObject1.get("value"))) {
                                    code = (code & true);    //所有断言判断都成功这个监控项才算成功，有一个失败就算失败
                                    errMsg = "";
                                } else {
                                    code &= false;
                                    errMsg = assJsonObject1.get("akey") + " = null";//断言，e.g. token=null
                                }
                                //取token
                                if (monitorItem.getClassify() == 1) {
                                    accessToken = (String) resJsonObject.get(assJsonObject1.get("akey"));
                                } else if (monitorItem.getClassify() == 3) {
                                    token = (String) resJsonObject.get(assJsonObject1.get("akey"));
                                }
                            } else {
                                if (resJsonObject.getString(assJsonObject1.getString("akey")).contains(assJsonObject1.getString("value"))) {
                                    code = (code & true);
                                    errMsg = "";
                                } else {
                                    code &= false;
                                    errMsg = assJsonObject1.get("akey") + " = " + resJsonObject.get(assJsonObject1.get("akey"));
                                }
                            }
                        }
                    }
                } else {
                    return Boolean.parseBoolean(null);
                }
            } else {
                return Boolean.parseBoolean(null);
            }
        }
        return code;
    }

    //classify为2或4时、monitorType为2(页面)时
    public void apiAndPagePro(MonitorItem monitorItem) {
        String url = monitorItem.getUrl();
        String asserts = monitorItem.getAsserts();
        code = true;   //0为失败，1为成功
        HttpHeaders requestHeaders = new HttpHeaders();
        Map requestBody = new HashMap();
        if (monitorItem.getClassify() == 2) {
            requestHeaders.add("Authorization", "Bearer " + accessToken);
        } else if (monitorItem.getClassify() == 4) {
            requestHeaders.add("Authorization", "Bearer " + token);
        }
        ResponseEntity<String> responseEntity = null;
        requestBody = (Map) JSON.parse(monitorItem.getRequestBody());
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        String body = null;

        //超时设置
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        if (monitorItem.getConnTimeout() != null) {
            simpleClientHttpRequestFactory.setConnectTimeout(monitorItem.getConnTimeout() * 1000);
        }
        RestTemplate restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        Integer requestType = monitorItem.getRequestType();
        int statusCodeValue;
        try {
            switch (requestType) {
                case 1:
                    responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                    break;
                case 2:
                    responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
                    break;
                case 3:
                    responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
                    break;
                case 4:
                    responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);
                    break;
                case 5:
                    responseEntity = restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, String.class);
                    break;
            }
            msgBody = responseEntity.getBody();//取返回体
            body = responseEntity.getBody();
            statusCodeValue = responseEntity.getStatusCodeValue();
        } catch (Exception e) {
            errMsg = "接口请求异常";
            msgBody = e.getMessage();//返回信息
            code = false;
            log.error("接口请求数据异常，在MonitorService的apiAndPagePro方法中，异常信息：", e);
            return;
        }
        if (statusCodeValue != 200) {
            errMsg = "接口请求异常";
            code = false;
            return;
        } else {
            if (body != null) {
                JSONObject resJsonObject = null;
                try {
                    resJsonObject = JSON.parseObject(body);//接口返回内容的json对象
                } catch (Exception e) {
                    if (monitorItem.getMonitorType() == 3) {
                        errMsg = "";
                    } else {
                        errMsg = "接口请求异常";
                    }
                    log.error("MonitorService的apiAndPagePro()中json格式数据转换异常");
                }
                if (resJsonObject != null) {
                    if (asserts != null) {
                        for (Object assJsonObject : JSON.parseArray(asserts)) {
                            JSONObject assJsonObject1 = (JSONObject) assJsonObject;
                            if (assJsonObject1.get("ststus").equals("0")) {
                                if (resJsonObject.get(assJsonObject1.get("akey")).equals(assJsonObject1.get("value"))) {
                                    code = (code & true);
                                    errMsg = "";
                                } else {
                                    code = (code & false);
                                    errMsg = assJsonObject1.get("akey") + " = " + resJsonObject.get(assJsonObject1.get("akey"));//断言，e.g. connect=false
                                }
                            } else if (assJsonObject1.get("ststus").equals("1")) {
                                if (!resJsonObject.get(assJsonObject1.get("akey")).equals(assJsonObject1.get("value"))) {
                                    code = (code & true);
                                    errMsg = "";
                                } else {
                                    code &= false;
                                    errMsg = assJsonObject1.get("akey") + " = null";//断言，e.g. token=null
                                }
                            } else {
                                if (resJsonObject.getString(assJsonObject1.getString("akey")).contains(assJsonObject1.getString("value"))) {
                                    code = (code & true);
                                    errMsg = "";
                                } else {
                                    code &= false;
                                    errMsg = assJsonObject1.get("akey") + " = " + resJsonObject.get(assJsonObject1.get("akey"));
                                }
                            }
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    //monitorType为3(视频)时
    public void videoPro(MonitorItem monitorItem) {
        code = true;
        //1、测试视频相关所有接口
        //2、测试视频文件获取
        String domain = monitorItem.getUrl();
        JSONObject params = (JSONObject) JSONObject.parse(monitorItem.getRequestBody());
        String ivsid = params.getString("ivsid");
        String channel = params.getString("channel");
        Integer connTimeout = monitorItem.getConnTimeout();
        Integer readTimeout = monitorItem.getReadTimeout();
        DetailReport monite = aVideoMmonit.monite(domain, ivsid, channel, connTimeout, readTimeout);

        errMsg = monite.getMessage();
        msgBody = monite.getMessageBody();
        code = monite.getCode();
    }
}
