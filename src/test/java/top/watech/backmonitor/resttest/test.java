package top.watech.backmonitor.resttest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
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
}
