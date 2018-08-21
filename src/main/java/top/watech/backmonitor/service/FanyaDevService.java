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
import org.springframework.web.client.RestTemplate;
import top.watech.backmonitor.entity.MonitorItem;
import top.watech.backmonitor.repository.MonitorItemRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fhm on 2018/8/7.
 * 先通过接口 /device/group 取得设备分组信息
 * 再遍历组id，将组id拼接到接口 /device/detail 上
 * 就取到了每个组里的所有设备
 * 取每个设备的linked，全为false则接口挂了
 * 个别设备为false则导出出错信息
 */
@Slf4j
@Service
public class FanyaDevService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    MonitorItemService monitorItemService;
    @Autowired
    MonitorItemRepository monitorItemRepository;

    //tooken
    public static String token;
    public static boolean devInfoCode = true;
    public static boolean totalCode = true;
    public static String devMsg = "";//出错详情（出错信息 + 断言部分）
//    public static StringBuilder msgBody = new StringBuilder("") ;//返回体
    public static boolean devConn = true;
    public static JSONObject msgBody;

    //生成token
    public void teToken() {
        MonitorItem fanyaLogin = monitorItemRepository.findByMonitorName("泛亚登录");
        String url = "https://api-pataciot-acniotsense.wise-paas.com.cn/api/v1.0/authentication/login/phone";
        HttpHeaders requestHeaders = new HttpHeaders();
        Map<String, Object> requestBody = new HashMap<String, Object>();
        String requestBodyStr = fanyaLogin.getRequestBody();
        requestBody = (Map) JSON.parse(requestBodyStr);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        JSONObject resJsonObject = JSON.parseObject(responseEntity.getBody());//接口返回内容的json对象

        token = resJsonObject.get("token").toString();
    }


    public void testDev() {
        teToken();  //取token
        /**
         * 取所有设备信息
         */
        String url2 ="https://api-pataciot-acniotsense.wise-paas.com.cn/api/v1.0/device/detail?userid=1";
        HttpHeaders requestHeaders2 = new HttpHeaders();
        requestHeaders2.add("Authorization","Bearer "+token);
        Map<String, Object> requestBody2 = new HashMap<String, Object>();
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity2 = new HttpEntity<Map<String, Object>>(requestBody2, requestHeaders2);
        ResponseEntity<String> responseEntity2 = restTemplate.exchange(url2,HttpMethod.GET,requestEntity2,String.class);
        msgBody = JSON.parseObject(responseEntity2.getBody());

        /**
         * 取逐个设备信息进行判断
         */
        String url = "https://api-pataciot-acniotsense.wise-paas.com.cn/api/v1.0/device/group";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer " + token);

        Map<String, Object> requestBody = new HashMap<String, Object>();
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        JSONObject resJsonObject = JSON.parseObject(responseEntity.getBody());//接口返回内容的json对象
//        System.err.println(resJsonObject);
        /**
         * 这里取到的resJsonObject对象，先取data中的内容get("data")
         * 再将data转成json数组，之后可以取出组id，组名
         * //循环组id
         * 将取到的id拼到 /device/detail?groupid=3&userid=1 下，生成新的URL
         * 然后取新接口的结果，判断connected
         */
//        String data = (String) resJsonObject.get("data");
//        JSONArray devGroup = JSON.parseArray(data); //data转成的json数组

        //循环1/2/3组所有设备
        for (int i = 1; i <= 3; i++) {
            System.out.println("*************************第" + i + "组设备情况**********************");
            String devUrl = "https://api-pataciot-acniotsense.wise-paas.com.cn/api/v1.0/device/detail?groupid=" + i + "&userid=1";
            HttpHeaders requestHeaders1 = new HttpHeaders();
            requestHeaders1.add("Authorization", "Bearer " + token);
            Map<String, Object> requestBody1 = new HashMap<String, Object>();
            HttpEntity<Map<String, Object>> requestEntity1 = new HttpEntity<Map<String, Object>>(requestBody1, requestHeaders1);

            ResponseEntity<String> responseEntity1 = restTemplate.exchange(devUrl, HttpMethod.GET, requestEntity1, String.class);
            JSONObject respBody = JSON.parseObject(responseEntity1.getBody());

//            msgBody.append(responseEntity1.getBody());//respBody.toJSONString()

            JSONArray data = (JSONArray) respBody.get("data");
            //断言数组循环判断,与监控项返回的JSON对象比对
            for (Object devInfoJsonObject : data) {
                JSONObject devInfoJsonObject1 = (JSONObject) devInfoJsonObject;
                Object connectedResult = devInfoJsonObject1.get("connected");
                if (connectedResult.equals(true)) {
                    devInfoCode = true;// + devInfoJsonObject1.get("position") +"]的设备:["
                    System.err.println("["+ devInfoJsonObject1.get("devicename") + "]:工作正常");
                    totalCode = totalCode & devInfoCode ;
                    devConn = devConn || devInfoCode;
                } else {
                    devInfoCode = false;// + devInfoJsonObject1.get("position") +"]的设备:["
                    int devStatus = (int)devInfoJsonObject1.get("status");
                    //设备状态
//                    String statusMsg = "";
//                    switch (devStatus){
//                        case 0:
//                            statusMsg = "设备状态：未知，连线状态：离线；";
//                        case 5:
//                            statusMsg = "设备状态：工作中，连线状态：离线；";
//                        case 6:
//                            statusMsg = "设备状态：空闲，连线状态：离线；";
//                        case 8:
//                            statusMsg = "设备状态：停机，连线状态：离线；";
//                    }
                    String s1 = "\n[" + devInfoJsonObject1.get("devicename") +
                            "]异常-" + "设备状态：未知，连线状态：离线;" ;// + "connected = "connectedResult + ";"
                    String s2 = "错误信息：" + responseEntity1.getBody();
                    devMsg = devMsg + s1 ;

//                    msgBody = "connected = " + connectedResult;
                    System.err.println(s1);
                    System.err.println(s2);
                    totalCode = totalCode & devInfoCode ;
                    devConn = devConn || devInfoCode;
                }
            }
        }
        if (totalCode == true) {
            System.out.println("***********************************************");
            System.err.println("************所有设备工作正常***********");
//            devMsg = "所有设备工作正常";
        }
        else if (devConn == false){
            System.out.println("***********************************************");
            System.err.println("************所有设备已掉线***********");
//            devMsg = "所有设备已掉线";
        }
        else {
            System.out.println("***********************************************");
            System.err.println("************设备信息获取接口工作异常***********");
//            devMsg = "设备信息获取接口工作异常";
        }
    }


}
