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

}
