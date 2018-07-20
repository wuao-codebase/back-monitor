package top.watech.backmonitor.resttest;

import com.alibaba.fastjson.JSONObject;
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

    @Test
    public void testSSO(String username,String password){
        String url = "http://portal-sso.wise-paas.com.cn/v1.3/auth/native";//请求地址
        String param ="?username=" + username + "&password=" + password;
        JSONObject json;
        json = JSONObject.parseObject(param);
        try{
            Map<String,Object> map=new HashMap<>();
            map.put("username","pataciot@aliyun.com");
            map.put("password","P@ssw0rd");
            String  str = restTemplate.postForObject(url+json,null, String.class,map);//所得结果为调整成String类型
        }catch(Exception e){
            System.out.println("登录失败");
            e.printStackTrace();
        }
    }

}
