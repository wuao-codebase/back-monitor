package top.watech.backmonitor.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuao.tp on 2018/7/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class Resttest {
    SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
    private  RestTemplate restTemplate;


        //无参get请求
      @Test public void get(){
            String url="https://www.baidu.com/";
          restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
            System.out.println( restTemplate.getForEntity(url, String.class).getBody());
            System.out.println( restTemplate.getForEntity(url, String.class).getStatusCode());

        }
    @Test
    public void getparms() throws Exception {
        restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        Map<String,String> multiValueMap = new HashMap<>();
        multiValueMap.put("username","lake");//传值，但要在url上配置相应的参数
        restTemplate.getForObject("/test/get?username={username}",String.class,multiValueMap);
    }


    @Test
    public void testSSO(){
        String url ="https://portal-sso.wise-paas.com.cn/v1.3/auth/native";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> requestBody = new HashMap<String, Object>();
        requestBody.put("username","pataciot@aliyun.com");
        requestBody.put("password", "P@ssw0rd");
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        //post
        ResponseEntity<String> responseEntity = restTemplate.exchange(url,HttpMethod.POST, requestEntity, String.class);
        System.out.println(responseEntity.getBody());
        JSONObject parse = JSON.parseObject(responseEntity.getBody());
        System.out.println(parse.get("accessToken").toString());
//        return parse.get("accessToken").toString();

    }


//    @Test
//    public void testVCM(){
//        String url ="https://api-vcm-acniotsense-patac.wise-paas.com.cn/vcm/vcm?enterprise_id=3";
//        HttpHeaders requestHeaders = new HttpHeaders();
//        requestHeaders.add("Authorization","Bearer "+testSSO());
//        Map<String, Object> requestBody = new HashMap<String, Object>();
//        //HttpEntity
//        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
//
//        ResponseEntity<String> responseEntity = restTemplate.exchange(url,HttpMethod.GET,requestEntity,String.class);
//        System.out.println("*************************************");
//        System.out.println(responseEntity);
//        System.out.println("*************************************");
//
//    }
//
//    @Test
//    public void testRMM(){
//        String url ="https://portal-rmm-acniotsense-patac.wise-paas.com.cn/rmm/v1/data/devices/5/latestdata_by_opts?agentId=00000001-0000-0000-0000-C400AD0365D7&plugin=Modbus_Handler&sensorId=/Platform/Connection";
//        HttpHeaders requestHeaders = new HttpHeaders();
//        requestHeaders.add("Authorization","Bearer "+testSSO());
//        Map<String, Object> requestBody = new HashMap<String, Object>();
//        //HttpEntitys
//        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
//
//        ResponseEntity<String> responseEntity = restTemplate.exchange(url,HttpMethod.GET,requestEntity,String.class);
//        System.out.println("*************************************");
//        System.out.println(responseEntity);
//        System.out.println("*************************************");
//    }

    public String testFanYa(){
        String url ="https://api-pataciot-acniotsense.wise-paas.com.cn/api/v1.0/authentication/login/phone";
        HttpHeaders requestHeaders = new HttpHeaders();
//        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> requestBody = new HashMap<String, Object>();
        requestBody.put("password", "123456");
        requestBody.put("phone","13900000000");
        requestBody.put("tokenTs", 20160);
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url,HttpMethod.POST,requestEntity,String.class);
        System.out.println("*************************************");
        System.out.println(responseEntity);
        JSONObject parse = JSON.parseObject(responseEntity.getBody());
        return parse.get("token").toString();
    }

    @Test
     public void testIndex(){
        String url ="https://portal-pataciot-acniotsense.wise-paas.com.cn";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization","Bearer "+testFanYa());
        Map<String, Object> requestBody = new HashMap<String, Object>();
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url,HttpMethod.GET,requestEntity,String.class);
        System.out.println("*************************************");
        System.out.println(responseEntity);
        System.out.println("*************************************");
    }

    @Test
    public void testSheBeiFenZu(){
        String url ="https://api-pataciot-acniotsense.wise-paas.com.cn/api/v1.0/device/group";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization","Bearer "+testFanYa());
        Map<String, Object> requestBody = new HashMap<String, Object>();
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url,HttpMethod.GET,requestEntity,String.class);
        System.out.println("*************************************");
        System.out.println(responseEntity);
        System.out.println("*************************************");
    }
    @Test
    public void testSheBeiXinxi(){
        String url ="https://api-pataciot-acniotsense.wise-paas.com.cn/api/v1.0/device/detail?groupid=3&userid=1";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization","Bearer "+testFanYa());
        Map<String, Object> requestBody = new HashMap<String, Object>();
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url,HttpMethod.GET,requestEntity,String.class);
        System.out.println("*************************************");
        System.out.println(responseEntity);
        System.out.println("*************************************");
    }

    @Test
    public void devInfo(){
        String url ="https://api-pataciot-acniotsense.wise-paas.com.cn/api/v1.0/device/detail?userid=1";//?groupid=3&userid=1
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization","Bearer "+testFanYa());
        Map<String, Object> requestBody = new HashMap<String, Object>();
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url,HttpMethod.GET,requestEntity,String.class);
        System.out.println("*************************************");
        System.out.println(responseEntity);
        System.out.println("*************************************");
    }


}
