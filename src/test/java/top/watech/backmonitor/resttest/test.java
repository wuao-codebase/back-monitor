package top.watech.backmonitor.resttest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import top.watech.backmonitor.BackMonitorApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuao.tp on 2018/7/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackMonitorApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class test {

        @Autowired
        private RestTemplate restTemplate;

        //无参get请求
      @Test public void get(){
            String url="https://www.baidu.com/";
            System.out.println( restTemplate.getForEntity(url, String.class).getBody());
            System.out.println( restTemplate.getForEntity(url, String.class).getStatusCode());

        }
    @Test
    public void getparms() throws Exception {
        Map<String,String> multiValueMap = new HashMap<>();
        multiValueMap.put("username","lake");//传值，但要在url上配置相应的参数
        restTemplate.getForObject("/test/get?username={username}",String.class,multiValueMap);
    }

    @Test
    public void testSSO(){
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("username","pataciot@aliyun.com");
        requestBody.put("password", "P@ssw0rd");
        //HttpEntity
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<Map<String, String>>(requestBody, requestHeaders);
        //post
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://portal-sso.wise-paas.com.cn/v1.3/auth/native", requestEntity, String.class);
        System.out.println(responseEntity);
    }

    @Test
    public void testVCM(){
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjbi13aXNlcGFhcyIsImlhdCI6MTUzMjMwNjMzNiwiZXhwIjoxNTMyMzA5OTM2LCJ1c2VySWQiOiJlYmUzYTU2YS0wZGVhLTRkNzctYmYxNS03NTMzZDVjMDNjNDciLCJjcmVhdGlvblRpbWUiOjE1MjU0MDA0ODkwMDAsImxhc3RNb2RpZmllZFRpbWUiOjE1MjU0MDA3MDQwMDAsInVzZXJuYW1lIjoicGF0YWNpb3RAYWxpeXVuLmNvbSIsImZpcnN0TmFtZSI6IlVzZXIiLCJsYXN0TmFtZSI6IkFub255bW91cyIsImNvbnRhY3RQaG9uZSI6Iis4NjEzNTcyMDY5NzM0IiwibW9iaWxlUGhvbmUiOiIrODYxMzU3MjA2OTczNCIsInJvbGUiOiJzcnBVc2VyIiwiZ3JvdXBzIjpbImhhaWxvbmcuZGFuZ0BhZHZhbnRlY2guY29tLmNuIl0sImNmU2NvcGVzIjpbeyJndWlkIjpudWxsLCJzc29fcm9sZSI6InNycFVzZXIiLCJzcGFjZXMiOltdfV0sInNjb3BlcyI6WyJWQ00tMTUyNTM5OTg1OTY4NC5hZG1pbiIsIlJNTS0xNTI1Mzk5NjU2Njg1LlN5c3RlbUFkbWluIiwiUk1NLTE1Mjc2NjI1MzAwOTYuU3lzdGVtQWRtaW4iLCJkYXNoYm9hcmQtMTUyNjQ0MjA1MTU0MC5BZG1pbiIsIlNhbXBsZVNSUC0xNTI4ODYyNzgzOTA1LmFkbWluIl0sInN0YXR1cyI6ImFjdGl2ZSIsIm9yaWdpbiI6IlNTTyIsInN5c3RlbSI6ZmFsc2UsInJlZnJlc2hUb2tlbiI6IjgyNWIwNWI0LWY5MDItNGU2NC1hMzU2LTk5MGJlNmUxOGFjNyJ9.6CxWdJl3GalUNxBcCBT7w3VjYiuFrXHnaDClnQebuDXbYKhtamLywxIWEHrayEXRM3B2O6kfVTQUh0a6Va4R6A");

        Map<String, String> requestBody = new HashMap<String, String>();
        //HttpEntity
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<Map<String, String>>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange("https://api-vcm-acniotsense-patac.wise-paas.com.cn/vcm/vcm?enterprise_id=3",HttpMethod.GET,requestEntity,String.class);
        System.out.println(responseEntity);
    }

}
