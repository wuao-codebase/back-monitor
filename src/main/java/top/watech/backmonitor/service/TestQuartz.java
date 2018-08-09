package top.watech.backmonitor.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;
import top.watech.backmonitor.entity.MonitorItem;
import top.watech.backmonitor.repository.MonitorItemRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fhm on 2018/7/24.
 * 执行定时任务
 */
public class TestQuartz extends QuartzJobBean {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    MonitorItemService monitorItemService;

    //tooken
    public static String token;

    //accessToken
    public static String accessToken;

    //成功总数
    public static int sucCount;

    //处理接口类型（RestTemplete）
    //取结果与断言比对，最后结果为code，成功还是失败
    public Integer apiMonitor(MonitorItem monitorItem) {
        String url = monitorItem.getUrl();
        Integer requestType = monitorItem.getRequestType();
        String asserts = monitorItem.getAsserts();

        int code = 1;   //0为失败，1为成功

        HttpHeaders requestHeaders = new HttpHeaders();
        String requestBodyStr = monitorItem.getRequestBody();
        Map requestBody = new HashMap();
        ResponseEntity<String> responseEntity = null;

        //POST型，登录生成token
        if (requestType == 1) {
            requestBody = (Map) JSON.parse(requestBodyStr);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            } catch (Exception e) {
                if (monitorItem.getClassify() == 1) {
                    System.err.println("SSO登录出错！");
                    System.err.println("错误信息：" + e.getMessage());
                    System.exit(0);
                } else {
                    System.err.println("SRP登录出错！");
                    System.err.println("错误信息：" + e.getMessage());
                    System.exit(0);
                }
            }
            JSONObject resJsonObject = JSON.parseObject(responseEntity.getBody());//接口返回内容的json对象

            int statusCodeValue = responseEntity.getStatusCodeValue();
            if (statusCodeValue != 200) {
                //登录一旦不成功，程序终止，退出
                System.err.println("退出");
                return null;
            }

            JSONArray assertsArraysJsonObject = JSON.parseArray(asserts);//传来的断言的json对象数组

            //断言数组循环判断,与监控项返回的JSON对象比对
            for (Object assJsonObject : assertsArraysJsonObject) {
                JSONObject assJsonObject1 = (JSONObject) assJsonObject;

                //ststus为0是等号，为1是不等号
                if (assJsonObject1.get("ststus").equals("0")) {
                    if (resJsonObject.get(assJsonObject1.get("akey")).equals(assJsonObject1.get("value"))) {
                        code = (code & 1);//成功
                    } else {
                        code = (code & 0);//失败
                    }
                } else {
                    if (!resJsonObject.get(assJsonObject1.get("akey")).equals(assJsonObject1.get("value")))
                        code = (code & 1);    //所有断言判断都成功这个监控项才算成功，有一个失败就算失败
                    else {
                        code &= 0;
                    }
                    if (monitorItem.getClassify() == 1) {
                        accessToken = (String) resJsonObject.get(assJsonObject1.get("akey"));
                    } else if (monitorItem.getClassify() == 3) {
                        token = (String) resJsonObject.get(assJsonObject1.get("akey"));
                    }
                }
            }
        }
        //GET请求，携带token访问
        else if (requestType == 2) {
            if (monitorItem.getClassify() == 2) {
                requestHeaders.add("Authorization", "Bearer " + accessToken);
            } else if (monitorItem.getClassify() == 4) {
                requestHeaders.add("Authorization", "Bearer " + token);
            }

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
                JSONObject resJsonObject = JSON.parseObject(responseEntity.getBody());//接口返回内容的json对象
                JSONArray assertsArraysJsonObject = JSON.parseArray(asserts);//传来的断言的json对象数组

                //断言数组循环判断
                for (Object assJsonObject : assertsArraysJsonObject) {
                    JSONObject assJsonObject1 = (JSONObject) assJsonObject;
                    if (assJsonObject1.get("ststus").equals("0")) {
                        //接口返回的对应asserts中value的值
                        String revalue = resJsonObject.get(assJsonObject1.get("akey")).toString();
                        if (revalue.equals(assJsonObject1.get("value")))
                            code = (code & 1);//成功
                        else {
                            code = (code & 0);//失败
                        }
                    } else {
                        if (!resJsonObject.get(assJsonObject1.get("akey")).equals(assJsonObject1.get("value")))
                            code = (code & 1);    //所有断言判断都成功这个监控项才算成功，有一个失败就算失败
                        else {
                            code = (code & 0);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("错误信息：" + e.getMessage());
                code = 0;
            }
        }
        if (code == 1) {
            System.err.println(monitorItem.getMonitorName() + ":" + "工作正常");
            System.err.println("*******************************************");
            sucCount = sucCount+1;
        } else {
            System.err.println(monitorItem.getMonitorName() + ":" + "工作异常");
            try {
                System.err.println("错误信息：xxxxxxxxxxx" + responseEntity.getBody());
            } catch (Exception e) {
                System.err.print("");
            }
            System.err.println("*******************************************");
        }
        return code;
    }

    //处理视频类型，最后结果是code，成功还是失败
    public void videoMonitor(MonitorItem monitorItem) {
        //1、测试视频相关所有接口
        //2、测试视频文件获取
        System.err.println(monitorItem.getMonitorName() + ":" + "不知道");
        System.err.println("*******************************************");
        sucCount = sucCount+1;
    }

    //处理页面类型，最后结果是code，成功还是失败 throws RestClientException
    public void pageMonitor(MonitorItem monitorItem) {
        int code = 1;   //0为失败，1为成功

        String url = monitorItem.getUrl();
        HttpHeaders requestHeaders = new HttpHeaders();
        Map requestBody = new HashMap();
        if (monitorItem.getClassify() == 2) {
            requestHeaders.add("Authorization", "Bearer " + accessToken);
        } else if (monitorItem.getClassify() == 4) {
            requestHeaders.add("Authorization", "Bearer " + token);
        }

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            code = 1;//成功
            System.err.println(monitorItem.getMonitorName() + ":" + "工作正常");
            System.err.println("*******************************************");
            sucCount = sucCount+1;
        } catch (Exception e) {
            code = 0;//失败
            System.err.println(monitorItem.getMonitorName() + ":" + "工作异常");
            System.err.println("错误信息：" + e.getMessage());
            System.err.println("*******************************************");
        }
    }

    /**
     * SRP监控流程
     * 1、SSO登录。2、平台接口。3、SRP登录。4、SRP下接口。
     */
    public void monitorLogic() {
        //开始时间
        System.err.println("开始时间:" + new Date());

        //SRP的所有监控项（sort by classify）
        List<MonitorItem> monitorItems = monitorItemService.getMonitTtemListBySrpId(66L);
        System.err.println("start!");
        for (MonitorItem monitorItem : monitorItems) {
            //平台登录
            if (monitorItem.getClassify() == 1) {

                apiMonitor(monitorItem);
            }
            //平台接口
            else if (monitorItem.getClassify() == 2) {
                //视频类型的监控项
                if (monitorItem.getMonitorType() == 2) {
                    videoMonitor(monitorItem);
                }
                //接口类型
                else if (monitorItem.getMonitorType() == 1) {
                    apiMonitor(monitorItem);
                }
                //页面监控
                else {
                    pageMonitor(monitorItem);
                }
            }

            //SRP登录
            if (monitorItem.getClassify() == 3) {
                apiMonitor(monitorItem);
            }
            //SRP接口
            else if (monitorItem.getClassify() == 4) {
                //视频类型的监控项
                if (monitorItem.getMonitorType() == 2) {
                    videoMonitor(monitorItem);
                }
                //接口类型
                else if (monitorItem.getMonitorType() == 1) {
                    apiMonitor(monitorItem);
                }
                //页面监控
                else {
                    pageMonitor(monitorItem);
                }
            }
        }
        System.err.println("监控项总个数："+monitorItems.size());
        System.err.println("监控项成功个数："+sucCount);
        System.err.println("*******************************************");
        sucCount = 0 ;
        testDev();

        //结束时间
        System.err.println("结束时间:" + new Date());
    }

    /**
     * 泛亚各分组下设备信息监控
     */
    @Autowired
    MonitorItemRepository monitorItemRepository;
    public static int devInfoCode = 1 ;

    public String testToken(){
        MonitorItem fanyaLogin = monitorItemRepository.findByMonitorName("fanyaLogin");
        String url ="http://api-pataciot-acniotsense.wise-paas.com.cn/api/v1.0/authentication/login/phone";
        HttpHeaders requestHeaders = new HttpHeaders();
        Map<String, Object> requestBody = new HashMap<String, Object>();
        String requestBodyStr = fanyaLogin.getRequestBody();
        requestBody = (Map) JSON.parse(requestBodyStr);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST,requestEntity,String.class);

        JSONObject resJsonObject = JSON.parseObject(responseEntity.getBody());//接口返回内容的json对象

        token = resJsonObject.get("token").toString();
        return token;
    }

    public void testDev(){
        String url ="http://api-pataciot-acniotsense.wise-paas.com.cn/api/v1.0/device/group";
        HttpHeaders requestHeaders = new HttpHeaders();
        token=testToken();
        requestHeaders.add("Authorization","Bearer "+token);

        Map<String, Object> requestBody = new HashMap<String, Object>();
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET,requestEntity,String.class);
        JSONObject resJsonObject = JSON.parseObject(responseEntity.getBody());//接口返回内容的json对象
//        System.err.println(resJsonObject);
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
            System.err.println("************所有设备工作正常***********");
        }else {
            System.out.println("***********************************************");
            System.err.println("************设备信息获取接口工作异常***********");
        }
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //写监控逻辑
        System.out.println("quart task-当前时间："+new Date());
        monitorLogic();
    }

}
