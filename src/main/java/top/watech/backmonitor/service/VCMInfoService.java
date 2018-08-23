package top.watech.backmonitor.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.watech.backmonitor.entity.MonitorItem;
import top.watech.backmonitor.entity.VCMInfo;
import top.watech.backmonitor.repository.MonitorItemRepository;
import top.watech.backmonitor.repository.VCMInfoRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fhm on 2018/8/7.
 * 取VCM信息，给视频监控用
 */
@Slf4j
@Service
public class VCMInfoService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    MonitorItemRepository monitorItemRepository;
    @Autowired
    VCMInfoRepository vcmInfoRepository;
    @Autowired
    MonitorService monitorService;

    //accessToken
    public static String accessToken;

    //生成accessToken
    public void getaccessToken() {
        String url = "https://portal-sso.wise-paas.com.cn/v1.3/auth/native";
        HttpHeaders requestHeaders = new HttpHeaders();
        Map<String, Object> requestBody = new HashMap<String, Object>();
        requestBody.put("username","pataciot@aliyun.com");
        requestBody.put("password", "P@ssw0rd");
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        String body = null;
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            body = responseEntity.getBody();
        }catch (Exception e){
            log.error("SSO登录接口取token异常，在VCMInfoService的getaccessToken方法中，错误信息：" + e);
        }

        if (body != null){
            JSONObject resJsonObject = JSON.parseObject(body);//接口返回内容的json对象
            accessToken = resJsonObject.get("accessToken").toString();
        }
        else {
            accessToken = new MonitorService().accessToken;
        }
    }

    public List<VCMInfo> getVCMInfos() throws Exception {
        String url = "https://api-vcm-acniotsense-patac.wise-paas.com.cn/vcm/vcm?enterprise_id=3x";
        HttpHeaders requestHeaders = new HttpHeaders();
        getaccessToken();
        requestHeaders.add("Authorization", "Bearer " + accessToken);

        Map<String, Object> requestBody = new HashMap<String, Object>();
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        String body = null;
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            body = responseEntity.getBody();
        }
        catch (Exception e){
            log.error("VCM接口取信息异常，在VCMInfoService的getVCMInfos方法中，错误信息：" + e);
        }
        if (body != null){
            JSONObject resJsonObject = JSON.parseObject(body);//接口返回内容的json对象
            System.err.println(resJsonObject);
            /**
             * 这里取到的resJsonObject对象，先取dashinfo中的内容get("dashinfo")
             * 再将dashinfo转成json数组.然后循环这个数组中的所有json对象
             * 之后取出每个json对象的password,domain,apiport,username.[get("xxx")]
             * 还有vmsinfo,它也是转json数组,然后再循环数组，对每个json对象取vmsname，ivsid,vcmid
             * 最后全存入数据库
             */

            //表清空
            if (vcmInfoRepository.findAll() != null) {
                vcmInfoRepository.deleteAll();
            }

            JSONArray dashinfo = (JSONArray) resJsonObject.get("dashinfo");//dashinfo的json数组
            for (Object dashObj : dashinfo) {
                JSONObject dashJsonObj = (JSONObject) dashObj;
                String password = (String) dashJsonObj.get("password");
                String domain = (String) dashJsonObj.get("domain");
                String apiport = (String) dashJsonObj.get("apiport");
                String username = (String) dashJsonObj.get("username");

                JSONArray vmsinfo = (JSONArray) dashJsonObj.get("vmsinfo");//对象转json数组
                String vmsname = "";
                String ivsid = "";
                String vcmid = "";
                for (Object vmsObj : vmsinfo) {
                    JSONObject vmsJsonObj = (JSONObject) vmsObj;
                    vmsname = (String) vmsJsonObj.get("vmsname");
                    ivsid = (String) vmsJsonObj.get("ivsid");
                    vcmid = (String) vmsJsonObj.get("vcmid");
                }

                VCMInfo vcmInfo = new VCMInfo();
                vcmInfo.setPassword(password);
                vcmInfo.setDomain(domain);
                vcmInfo.setApiport(apiport);
                vcmInfo.setUsername(username);
                vcmInfo.setVmsname(vmsname);
                vcmInfo.setIvsid(ivsid);
                vcmInfo.setVcmid(vcmid);
                vcmInfoRepository.save(vcmInfo);
            }
            return vcmInfoRepository.findAll();
        }
        else {
            List<VCMInfo> vcmInfoList = vcmInfoRepository.findAll();
            return vcmInfoList;
        }
    }
}
