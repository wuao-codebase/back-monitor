package top.watech.backmonitor.test;

import com.alibaba.fastjson.JSON;
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

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class WeixinTest {

    @Autowired
    private RestTemplate restTemplate;

    public String testTOKEN(){
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
       return parse.get("access_token").toString();
    }

    @Test
    public void testSEND(){
        String url ="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=9XAV6JiVutn6U9JkrmoinYHCuazyDQlHAo1x-cgWL7V2bz89CRTSKEFIWTBVhscn8tYgEkepvmBuFirfC8DvWYpRX_y0AtJO88k6IeZxqq3m1P801OaBdl3LIr7lKxB8tJ6cVnoouNaD4Z4LKmD52W0NfSS3KLt82Xw_1q7Pb3izhGYuCCCh6Q1JaQtU8M2kaRGMfkliP21iQajJAVThHQ";
        HttpHeaders requestHeaders = new HttpHeaders();
        Map<String, Object> requestBody = new HashMap<String, Object>();
        Map<String, Object> msg = new HashMap<String, Object>();
        msg.put("content","你的快递已到，请携带工卡前往邮件中心领取。出发前可查看邮件中心视频实况，聪明避开排队。");
        JSONObject itemJSONObj = JSONObject.parseObject(JSON.toJSONString(msg));
        requestBody.put("touser","111");
        requestBody.put("msgtype", "text");
        requestBody.put("agentid",1000002);
        requestBody.put("text", itemJSONObj);
        requestBody.put("safe", 0);


        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url,HttpMethod.GET,requestEntity,String.class);
        System.out.println("*************************************");
        System.out.println(responseEntity);
        System.out.println("*************************************");

    }
}
