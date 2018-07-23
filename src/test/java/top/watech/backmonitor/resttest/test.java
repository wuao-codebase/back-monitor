package top.watech.backmonitor.resttest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import top.watech.backmonitor.BackMonitorApplication;
import top.watech.backmonitor.entity.User;
import top.watech.backmonitor.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuao.tp on 2018/7/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackMonitorApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class test {

    /*
     *  API Test
     */

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
        requestHeaders.setContentType(MediaType.TEXT_HTML);
        requestHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjbi13aXNlcGFhcyIsImlhdCI6MTUzMjA3Nzc3MiwiZXhwIjoxNTMyMDgxMzcyLCJ1c2VySWQiOiJlYmUzYTU2YS0wZGVhLTRkNzctYmYxNS03NTMzZDVjMDNjNDciLCJjcmVhdGlvblRpbWUiOjE1MjU0MDA0ODkwMDAsImxhc3RNb2RpZmllZFRpbWUiOjE1MjU0MDA3MDQwMDAsInVzZXJuYW1lIjoicGF0YWNpb3RAYWxpeXVuLmNvbSIsImZpcnN0TmFtZSI6IlVzZXIiLCJsYXN0TmFtZSI6IkFub255bW91cyIsImNvbnRhY3RQaG9uZSI6Iis4NjEzNTcyMDY5NzM0IiwibW9iaWxlUGhvbmUiOiIrODYxMzU3MjA2OTczNCIsInJvbGUiOiJzcnBVc2VyIiwiZ3JvdXBzIjpbImhhaWxvbmcuZGFuZ0BhZHZhbnRlY2guY29tLmNuIl0sImNmU2NvcGVzIjpbeyJndWlkIjpudWxsLCJzc29fcm9sZSI6InNycFVzZXIiLCJzcGFjZXMiOltdfV0sInNjb3BlcyI6WyJWQ00tMTUyNTM5OTg1OTY4NC5hZG1pbiIsIlJNTS0xNTI1Mzk5NjU2Njg1LlN5c3RlbUFkbWluIiwiUk1NLTE1Mjc2NjI1MzAwOTYuU3lzdGVtQWRtaW4iLCJkYXNoYm9hcmQtMTUyNjQ0MjA1MTU0MC5BZG1pbiIsIlNhbXBsZVNSUC0xNTI4ODYyNzgzOTA1LmFkbWluIl0sInN0YXR1cyI6ImFjdGl2ZSIsIm9yaWdpbiI6IlNTTyIsInN5c3RlbSI6ZmFsc2UsInJlZnJlc2hUb2tlbiI6Ijg2OGMwMTg4LTk0Y2UtNDQxOS04ZjNmLWQzMmYwZTBlNDVhNiJ9._cTp1e1GEVxRZNLiE68i0_qNJDDgEiDi6SbyyKtW9KrrRiOvYtAc2bKcG5h4mTR9zlnQSP5gbUu1q1etlqjkxw");
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("enterprise_id","3");
        //HttpEntity
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<Map<String, String>>(requestBody, requestHeaders);
        //post
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("https://api-vcm-acniotsense-patac.wise-paas.com.cn/vcm/vcm", String.class, requestEntity);
        System.out.println(responseEntity);
    }


    /*
     *  Page Test
     */

    private UserService userService ;
    @Test
    public void testSaveAll(){
        List<User> users = new ArrayList<>();

        for (int i = 'a' ; i<'z' ; i ++){
            User user = new User();
            user.setUserId(i+1);
            user.setUsername((char)i + "" + (char)i);
            user.setUserpwd("111111");

            users.add(user);
        }
        userService.saveUsers(users);

    }
}
