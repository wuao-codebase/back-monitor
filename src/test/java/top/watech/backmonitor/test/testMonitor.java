package top.watech.backmonitor.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import top.watech.backmonitor.entity.MonitorItem;
import top.watech.backmonitor.service.MonitorItemService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fhm on 2018/8/6.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class testMonitor {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    MonitorItemService monitorItemService;

    //tooken
    public static String  token;

    //处理页面和接口类型（RestTemplete）
    //取结果与断言比对，最后结果为code，成功还是失败
    public Integer testPageAndApi(MonitorItem monitorItem){
        String url=monitorItem.getUrl();
        Integer requestType=monitorItem.getRequestType();
        String asserts = monitorItem.getAsserts();

        int code = 0;   //成功还是失败

        HttpHeaders requestHeaders = new HttpHeaders();

        String requestBodyStr = monitorItem.getRequestBody();

        Map requestBody = new HashMap();
        //POST型，登录生成token
        if(requestType==1){
            requestBody = (Map) JSON.parse(requestBodyStr);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            JSONObject resJsonObject = JSON.parseObject(responseEntity.getBody());//接口返回内容的json对象

            int statusCodeValue = responseEntity.getStatusCodeValue();
            String statusCodeValue1 = String.valueOf(statusCodeValue);
            JSONObject statusjsonObject = JSON.parseObject(statusCodeValue1);

            System.err.println(monitorItem.getMonitorName());
            System.err.println(resJsonObject);
            System.err.println(monitorItem.getAsserts());

            JSONArray assertsArraysJsonObject = JSON.parseArray(asserts);//传来的断言的json对象数组

            //断言数组循环判断,与监控项返回的JSON对象比对
            for ( Object assJsonObject : assertsArraysJsonObject){
                JSONObject assJsonObject1 = (JSONObject) assJsonObject;

                System.err.println("断言数组循环判断等号");
                System.err.println(assJsonObject1.get("ststus"));

                //status为0是等号，为1是不等号
                if(assJsonObject1.get("ststus").equals("0")){

                    System.err.println("等号");

//                    if(resJsonObject.get(assJsonObject1.get("key")).equals(assJsonObject1.get("value")))

                    System.err.println(asserts);

                    if(statusjsonObject.equals(assJsonObject1.get("value"))){
                        System.err.println("200");

                        code=(code & 0);//成功
                    }
                    code=(code & 1);//失败
                }
                else if(assJsonObject1.get("status").equals("1")){

                    System.err.println("不等号");

                    if(!resJsonObject.get(assJsonObject1.get("key")).equals(assJsonObject1.get("value")))

                        System.err.println("另一个条件");

                        code=(code & 0);    //所有断言判断都成功这个监控项才算成功，有一个失败就算失败
                    code &= 1;
                }
            }
        }
        //GET请求，携带token访问
        else if (requestType==2){
            requestHeaders.add("Authorization","Bearer "+token);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);


            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            //int statusCodeValue = responseEntity.getStatusCodeValue();
            JSONObject resJsonObject = JSON.parseObject(responseEntity.getBody());//接口返回内容的json对象

            JSONArray assertsArraysJsonObject = JSON.parseArray(asserts);//传来的断言的json对象数组

            //断言数组循环判断
            for ( Object assJsonObject : assertsArraysJsonObject){
                JSONObject assJsonObject1 = (JSONObject) assJsonObject;

                if(assJsonObject1.get("ststus")=="0"){
                    if(resJsonObject.get(assJsonObject1.get("key"))==assJsonObject1.get("value"))
                        code=(code & 0);//成功
                    code=(code & 1);//失败
                }
                else if(assJsonObject1.get("status")=="1"){
                    if(resJsonObject.get(assJsonObject1.get("key"))!=assJsonObject1.get("value"))
                        code=(code & 0);    //所有断言判断都成功这个监控项才算成功，有一个失败就算失败
                    code=(code & 1);
                }
            }
        }
        System.err.println("code");
        return code;
    }

    //处理视频类型，最后结果是code，成功还是失败
    public void testVideo(){
        //1、测试视频相关所有接口
        //2、测试视频文件获取


    }

    @Test
    public void monitorLogic(){
        System.err.println("start1");
        //SRP的所有监控项（sort by classify）

        List<MonitorItem> monitorItems = monitorItemService.getMonitTtemListBySrpId(66L);

        for (MonitorItem monitorItem:monitorItems){
            //平台登录
            if (monitorItem.getClassify()==1){

                System.out.println(testPageAndApi(monitorItem));
                System.err.println("1111111");
            }
            //平台接口
            else if (monitorItem.getClassify()==2){
                System.err.println("222222222");

                //视频类型的监控项
                if (monitorItem.getMonitorType()==2){
                    //testVideo
                    System.out.println("不知道");

                }
                //页面或接口类型
                else{
                    System.out.println(testPageAndApi(monitorItem));
                }
            }

            //SRP登录
            if (monitorItem.getClassify()==3){
                testPageAndApi(monitorItem);
            }
            //SRP接口
            else if (monitorItem.getClassify()==4){
                //视频类型的监控项
                if (monitorItem.getMonitorType()==2){
                    //testVideo
                    System.out.println("不知道");

                }
                //页面或接口类型
                else{
                    System.out.println(testPageAndApi(monitorItem));
                }
            }
        }

    }

}
