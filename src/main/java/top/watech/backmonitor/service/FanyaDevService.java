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
 * 泛亚设备的监控
 */
@Slf4j
@Service
public class FanyaDevService {
    @Autowired
    private RestTemplate restTemplate;

    public static String token;
    public static boolean devInfoCode = true;
    public static boolean totalCode = true;
    public static String devMsg = "";//出错详情（出错信息 + 断言部分）
    public static boolean devConn = true;
    public static JSONObject msgBody;//返回体

    /**
     * 生成token
     */
    public void teToken() {
        String url = "https://api-pataciot-acniotsense.wise-paas.com.cn/api/v1.0/authentication/login/phone";
        HttpHeaders requestHeaders = new HttpHeaders();
        Map<String, Object> requestBody = new HashMap<String, Object>();
        requestBody.put("password", "123456");
        requestBody.put("phone", "13900000000");
        requestBody.put("tokenTs", 20160);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        String body = null;
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            body = responseEntity.getBody();
        } catch (Exception e) {
            log.error("泛亚设备取信息时取token异常，错误信息：" + e);
        }
        if (body != null) {
            JSONObject resJsonObject = JSON.parseObject(body);//接口返回内容的json对象
            token = resJsonObject.get("token").toString();
        } else {
            log.error("泛亚设备取信息时取token异常");
        }
    }

    /**
     * 设备监控逻辑
     */
    public void testDev() {
        teToken();  //取token
        //取所有设备信息
        String url2 = "https://api-pataciot-acniotsense.wise-paas.com.cn/api/v1.0/device/detail?userid=1";
        HttpHeaders requestHeaders2 = new HttpHeaders();
        requestHeaders2.add("Authorization", "Bearer " + token);
        Map<String, Object> requestBody2 = new HashMap<String, Object>();
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity2 = new HttpEntity<Map<String, Object>>(requestBody2, requestHeaders2);
        String body = null;
        try {
            ResponseEntity<String> responseEntity2 = restTemplate.exchange(url2, HttpMethod.GET, requestEntity2, String.class);
            body = responseEntity2.getBody();
        } catch (Exception e) {
            log.error("泛亚设备取信息时取token异常，错误信息：" + e);
        }
        if (body != null) {
            msgBody = JSON.parseObject(body);
        } else {
            devInfoCode = false;
            devMsg = "设备已全部离线";
            totalCode = false;
        }

        /**
         * 取逐个设备信息进行判断
         * 这里取到的resJsonObject对象，先取data中的内容get("data")
         * 再将data转成json数组，之后可以取出组id，组名
         * //循环组id
         * 将取到的id拼到 /device/detail?groupid=3&userid=1 下，生成新的URL
         * 然后取新接口的结果，判断connected
         */
        //循环1/2/3组所有设备
        for (int i = 1; i <= 3; i++) {
            System.out.println("*************************第" + i + "组设备情况**********************");
            String devUrl = "https://api-pataciot-acniotsense.wise-paas.com.cn/api/v1.0/device/detail?groupid=" + i + "&userid=1";
            HttpHeaders requestHeaders1 = new HttpHeaders();
            requestHeaders1.add("Authorization", "Bearer " + token);
            Map<String, Object> requestBody1 = new HashMap<String, Object>();
            HttpEntity<Map<String, Object>> requestEntity1 = new HttpEntity<Map<String, Object>>(requestBody1, requestHeaders1);

            String body1 = null;
            try {
                ResponseEntity<String> responseEntity1 = restTemplate.exchange(devUrl, HttpMethod.GET, requestEntity1, String.class);
                body1 = responseEntity1.getBody();
            } catch (Exception e) {
                log.error("泛亚设备监控异常，错误信息：" + e);
            }
            if (body1 != null) {
                JSONObject respBody = JSON.parseObject(body1);
                JSONArray data = (JSONArray) respBody.get("data");
                //断言数组循环判断,与监控项返回的JSON对象比对
                for (Object devInfoJsonObject : data) {
                    JSONObject devInfoJsonObject1 = (JSONObject) devInfoJsonObject;
                    Object connectedResult = devInfoJsonObject1.get("connected");
                    if (connectedResult.equals(true)) {
                        devInfoCode = true;// + devInfoJsonObject1.get("position") +"]的设备:["
                        System.err.println("[" + devInfoJsonObject1.get("devicename") + "]:工作正常");
                        totalCode = totalCode & devInfoCode;
                        devConn = devConn || devInfoCode;
                    } else {
                        devInfoCode = false;// + devInfoJsonObject1.get("position") +"]的设备:["
                        int devStatus = (int) devInfoJsonObject1.get("status");

                        String s1 = "\n[" + devInfoJsonObject1.get("devicename") +
                                "]异常-" + "设备状态：未知，连线状态：离线;";
                        String s2 = "错误信息：" + body1;
                        devMsg = devMsg + s1;

                        System.err.println(s1);
                        System.err.println(s2);
                        totalCode = totalCode & devInfoCode;
                        devConn = devConn || devInfoCode;
                    }
                }
            } else {
                log.error("泛亚设备监控异常");
                devInfoCode = false;
                devMsg = "设备已全部离线";
                totalCode = false;
            }
        }
    }
}
