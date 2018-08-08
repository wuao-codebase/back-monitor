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
import top.watech.backmonitor.repository.MonitorItemRepository;
import top.watech.backmonitor.service.MonitorItemService;

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
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class testDev {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    MonitorItemService monitorItemService;
    @Autowired
    MonitorItemRepository monitorItemRepository;

    //tooken
    public static String  token;
    public static int devInfoCode = 1 ;

    public String testToken(){
        MonitorItem fanyaLogin = monitorItemRepository.findByMonitorName("fanyaLogin");
        String url ="http://api-pataciot-acniotsense.wise-paas.com.cn/api/v1.0/authentication/login/phone";
        HttpHeaders requestHeaders = new HttpHeaders();
        Map<String, Object> requestBody = new HashMap<String, Object>();
        String requestBodyStr = fanyaLogin.getRequestBody();
        requestBody = (Map) JSON.parse(requestBodyStr);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url,HttpMethod.POST,requestEntity,String.class);

        JSONObject resJsonObject = JSON.parseObject(responseEntity.getBody());//接口返回内容的json对象

        token = resJsonObject.get("token").toString();
        return token;
    }

    @Test
    public void test1(){

        System.err.println(testToken());
    }

    @Test
    public void testDev(){
        String url ="http://api-pataciot-acniotsense.wise-paas.com.cn/api/v1.0/device/group";
        HttpHeaders requestHeaders = new HttpHeaders();
        token=testToken();
        requestHeaders.add("Authorization","Bearer "+token);

        Map<String, Object> requestBody = new HashMap<String, Object>();
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET,requestEntity,String.class);
        JSONObject resJsonObject = JSON.parseObject(responseEntity.getBody());//接口返回内容的json对象
        System.err.println(resJsonObject);
        /**
         * 这里取到的resJsonObject对象，先取data中的内容get("data")
         * 再将data转成json数组，之后可以取出组id，组名
         * //循环组id
         * 将取到的id拼到 /device/detail?groupid=3&userid=1 下，生成新的URL
         * 然后取新接口的结果，判断linked
         */
//        String data = (String) resJsonObject.get("data");
//        JSONArray devGroup = JSON.parseArray(data); //data转成的json数组

        //循环1/2/3组所有设备
        for (int i = 1 ; i <= 3 ; i ++) {
            System.out.println("*************************第"+i+"组设备情况**********************");
            String devUrl = "http://api-pataciot-acniotsense.wise-paas.com.cn/api/v1.0/device/detail?groupid=" + i + "&userid=1";
            HttpHeaders requestHeaders1 = new HttpHeaders();
            requestHeaders1.add("Authorization", "Bearer " + token);
            Map<String, Object> requestBody1 = new HashMap<String, Object>();
            HttpEntity<Map<String, Object>> requestEntity1 = new HttpEntity<Map<String, Object>>(requestBody1, requestHeaders1);

            ResponseEntity<String> responseEntity1 = restTemplate.exchange(devUrl, HttpMethod.GET, requestEntity1, String.class);
            JSONObject respBody = JSON.parseObject(responseEntity1.getBody());
//            System.err.println(respBody);

            JSONArray data = (JSONArray) respBody.get("data");
            //断言数组循环判断,与监控项返回的JSON对象比对
            for (Object devInfoJsonObject : data) {
                JSONObject devInfoJsonObject1 = (JSONObject) devInfoJsonObject;
                if (devInfoJsonObject1.get("linked").equals(true)) {
                    devInfoCode = 1;
                    System.err.println("["+devInfoJsonObject1.get("position")+
                    "]的设备:["+devInfoJsonObject1.get("devicename")+"]:工作正常");
                }
                else {
                    devInfoCode = 0;
                    System.err.println("["+devInfoJsonObject1.get("position")+
                            "]的设备:["+devInfoJsonObject1.get("devicename")+
                            "]:工作异常");
                    System.err.println("错误信息："+ responseEntity1.getBody());
                }
            }
            devInfoCode &= 1;
        }
        if (devInfoCode==1){
            System.out.println("***********************************************");
            System.err.println("所有设备工作正常");
        }else {
            System.out.println("***********************************************");
            System.err.println("************设备信息获取接口工作异常***********");
        }
    }

}
