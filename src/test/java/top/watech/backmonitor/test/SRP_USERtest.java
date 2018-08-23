package top.watech.backmonitor.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

/**
 * Created by wuao.tp on 2018/7/30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class SRP_USERtest {


    @Test
    public void  tests(){
        HashMap<String, String> map = new HashMap<>();
        map.put("123","");
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(map));
        if (jsonObject.getString("123").length()>0) {
            System.out.println("sss"+jsonObject.get("123"));
        }else
        {
            System.out.println("hehe");
        }
    }
@Test
    public  void test() {
        int count = 5;
        try {
            if (count==5){
            throw new RuntimeException("测试异常1");}
        }catch (Exception e) {
            System.out.println(e.toString());
            return;
        }
       System.out.println("12313");
        return;
    }
    }
