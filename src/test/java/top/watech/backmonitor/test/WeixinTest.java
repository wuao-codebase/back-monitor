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
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.repository.SrpRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class WeixinTest {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    SrpRepository srpRepository;

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
    public void  testToken(){
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
//        return parse.get("access_token").toString();
    }

    @Test
    public void testSEND(){
        String url ="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=baJC6I-RGRBDqB_tAKH9wFwTQplCqarCiujOsKm45dJh5XtoB_57WZghzgUDTtzt02fawtKJDiPe3w5cnAYEPd5ZuotVQMTmLK3bRtlKU8l8q3u7jdG6eNH1dEcKuyYxn1QUgbO5i73Dcf4MLL-4w5QUAzQsmNhGSOSBlE6r-wLhQAD7PP_GK_vAggn6P9X1DXyKu8coLyheap6-dpV1pQ";
        HttpHeaders requestHeaders = new HttpHeaders();
        Map<String, Object> requestBody = new HashMap<String, Object>();
        Map<String, Object> msg = new HashMap<String, Object>();

        SRP srp = srpRepository.findBySrpId(66L);

        String str = "【异常通知】\n\n·SRP名称：" + srp.getSrpName() + "\n" +
                    "·总测试项：" + 7 + "| " + "成功：" + 5 + "| " + "失败：" + 2 + "\n" +
                    "·测试开始时间：" + new Date() + "\n" +
                    "·测试失败监控项：" + "\n" +
                    "·测试结束时间：" + new Date() + "\n"
                ;

        msg.put("content",str);
        JSONObject itemJSONObj = JSONObject.parseObject(JSON.toJSONString(msg));
        requestBody.put("touser","111");
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
