package top.watech.backmonitor.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.watech.backmonitor.entity.*;
import top.watech.backmonitor.repository.MonitorItemRepository;
import top.watech.backmonitor.repository.UserRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by fhm on 2018/8/7.
 * 先通过接口 /device/group 取得设备分组信息
 * 再遍历组id，将组id拼接到接口 /device/detail 上
 * 就取到了每个组里的所有设备
 * 取每个设备的linked，全为false则接口挂了
 * 个别设备为false则导出出错信息
 */
@Service
public class WeixinSendService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    DetailReportService detailReportService;
    @Autowired
    MonitorItemRepository monitorItemRepository;
    @Autowired
    UserRepository userRepository;

    //tooken
    public static String token;
    public static String weixinErrmsg = "";
    public static String errorNotice = "";

    //微信token
    public void testToken(){
        //企业ID + 小程序的Secret
        String url ="https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=ww12e8f8024d09e7e0&corpsecret=nvgY1oY07Xq-BHps0XSdCULaf36BYzIBEn5Rz58fB0I";
        HttpHeaders requestHeaders = new HttpHeaders();
        Map<String, Object> requestBody = new HashMap<String, Object>();
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url,HttpMethod.GET,requestEntity,String.class);
        JSONObject parse = JSON.parseObject(responseEntity.getBody());
        System.out.println("*************************************");
        System.err.println(parse.get("access_token"));
        System.out.println("*************************************");
        token = parse.get("access_token").toString();
    }

    public void weixinSend(TotalReport totalReport) {
        testToken();
        String url ="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+token;
        HttpHeaders requestHeaders = new HttpHeaders();
        Map<String, Object> requestBody = new HashMap<String, Object>();
        Map<String, Object> msg = new HashMap<String, Object>();

        SRP srp = totalReport.getSrp();
        SimpleDateFormat str1 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Integer monitorNum = totalReport.getMonitorNum();
        Integer errorCount = totalReport.getErrorCount();
        int sucCount = monitorNum - errorCount;
        Date startTime = totalReport.getStartTime();
        String formatStartTime = str1.format(startTime);
        Date endTime = totalReport.getEndTime();
        String formatEndTime = str1.format(endTime);

        List<DetailReport> detailReportList = detailReportService.getDetailReportByUuid(totalReport.getUuid());
        int i = 1 ;
        for (DetailReport detailReport : detailReportList){
            MonitorItem monitorItem = monitorItemRepository.findByMonitorId(detailReport.getMonitorId());
            String monitorName = monitorItem.getMonitorName();
            if (!detailReport.getCode()){
                String errMessage = detailReport.getMessage();
                weixinErrmsg = weixinErrmsg + "  (" + i + ")" + monitorName + "接口,返回异常，返回结果：" + errMessage + "\n" ;
                if (!"设备信息获取".equals(monitorName)){
                    errorNotice = errorNotice + "\n" + monitorName + "接口异常" ;
                }
                else {
                    String str = detailReport.getMessage();
                    String[] strings = StringUtils.substringsBetween(str, "[", "]");
//                    errorNotice = errorNotice + detailReport.getMessage() ;
                    for (String s : strings){
                        errorNotice = errorNotice + "\n[" + s + "]异常";
                    }

                }

                i++;
            }
            else {
                weixinErrmsg = weixinErrmsg + "  (" + i + ")" + monitorName + "接口,返回正常；\n" ;
                i++;
            }
        }

        String str ="【异常通知】" + errorNotice + "\n\n【监控日志】\n·SRP名称：" + srp.getSrpName() + "\n" +
                "·总测试项：" + monitorNum + "| " + "成功：" + sucCount + "| " + "失败：" + errorCount + "\n" +
                "·测试开始时间：" + formatStartTime + "\n" +
                "·各监控项状态：" + "\n" +
                weixinErrmsg +
                "·测试结束时间：" + formatEndTime + "\n\n" +
                "(详细监控信息请前往网页端查看)" + "\n"
                ;

        //SRP所属用户全推送
        Set<User> users = srp.getUsers();
        StringBuilder s = new StringBuilder("") ;
        if (users != null){
            for (User user : users){
                String s1 = String.valueOf(user.getUserId());
                s.append(s1);
                s.append("|");
                char c = s.charAt(s.length() - 1);
            }
        }

        //超级用户全推送
        List<User> userList = userRepository.findAll();
        for (User user : userList){
            String s1 = String.valueOf(user.getUserId());
            s.append(s1);
            s.append("|");
        }

        s.deleteCharAt(s.length()-1);
        System.err.println(s);


        msg.put("content",str);
        JSONObject itemJSONObj = JSONObject.parseObject(JSON.toJSONString(msg));
//        requestBody.put("touser","111|WuAo");
        requestBody.put("touser",s);
        requestBody.put("msgtype", "text");
        requestBody.put("agentid",1000002);
        requestBody.put("text", itemJSONObj);
        requestBody.put("safe", 0);


        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url,HttpMethod.POST,requestEntity,String.class);
        System.err.println("*************************************");
        System.err.println(responseEntity);
        System.err.println("*************************************");


    }


}
