package top.watech.backmonitor.test;

/**
 * Created by wuao.tp on 2018/8/13.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import top.watech.backmonitor.service.AVideoMmonit;

import java.text.SimpleDateFormat;
import java.util.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class testVideo {


    SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
    private  RestTemplate restTemplate;
    private Map<String,String> map = new HashMap<String,String>();

    @Test
    public void testdate() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -1);
        long starttime = now.getTimeInMillis() / 1000;
        long endtime = starttime + 300;
//        String  yestedayDate
//
//                = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now.getTime());
//        System.out.println("yestedayDate = " + yestedayDate);

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000); //10位时间戳
        System.out.println("timestamp = " + timestamp);

        long timeStamp = System.currentTimeMillis();  //获取当前13位时间戳,也可以是你自已给的一个随机的或是别人给你的时间戳(一定是long型的数据)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
        String sd = sdf.format(new Date(timeStamp));   // 时间戳转换成时间
        System.out.println(sd);//打印出你要的时间
    }

    public String  SSO(){
        String url ="http://portal-sso.wise-paas.com.cn/v1.3/auth/native";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> requestBody = new HashMap<String, Object>();
        requestBody.put("username","pataciot@aliyun.com");
        requestBody.put("password", "P@ssw0rd");
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        //post
        ResponseEntity<String> responseEntity=null;
        restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        try {
        responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        } catch (Exception e) {
            System.err.println("SSO登陆失败");
            System.err.println("错误信息：" + e.getMessage());
        }

        JSONObject parse = JSON.parseObject(responseEntity.getBody());
        if (parse.get("accessToken")!=null){
            System.err.println("accessToken获取成功");
            return parse.get("accessToken").toString();
        }
        return  null;
    }


    public String VCM(String token){
        String url ="https://api-vcm-acniotsense-patac.wise-paas.com.cn/vcm/vcm?enterprise_id=3";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization","Bearer "+token);
        Map<String, Object> requestBody = new HashMap<String, Object>();
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        ResponseEntity<String> responseEntity=null;
        restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        try {
            responseEntity = restTemplate.exchange(url,HttpMethod.GET,requestEntity,String.class);

        } catch (Exception e) {
            System.err.println("VCM请求出错");
            System.err.println("错误信息：" + e.getMessage());
        }
        JSONObject parse = JSON.parseObject(responseEntity.getBody());
        if (parse.get("dashinfo")!=null){
            System.err.println("dashinfo获取成功");
            return parse.get("dashinfo").toString();
        }else {
            System.err.println("dashinfo获取为空");
        }
        return  null;
    }

    public String FanYa(){
        String url ="http://api-pataciot-acniotsense.wise-paas.com.cn/api/v1.0/authentication/login/phone";
        HttpHeaders requestHeaders = new HttpHeaders();
//        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> requestBody = new HashMap<String, Object>();
        requestBody.put("password", "123456");
        requestBody.put("phone","13900000000");
        requestBody.put("tokenTs", 20160);
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url,HttpMethod.POST,requestEntity,String.class);
        JSONObject parse = JSON.parseObject(responseEntity.getBody());
        return parse.get("token").toString();
    }

    public void SheBeiXinxi(String token){
        String url ="http://api-pataciot-acniotsense.wise-paas.com.cn/api/v1.0/device/detail?userid=1";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization","Bearer "+token);
        Map<String, Object> requestBody = new HashMap<String, Object>();
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url,HttpMethod.GET,requestEntity,String.class);
        System.out.println(responseEntity);
        JSONObject parse = JSON.parseObject(responseEntity.getBody());
        JSONArray data = (JSONArray) JSONArray.parse(parse.getString("data"));
        for (Object datum : data) {
            JSONObject parse1 = (JSONObject) JSONObject.parse(datum.toString());
            if(parse1.getString("agentid").trim().length()>0 && parse1.getString("position").trim().length()>0 && parse1.getString("devicename").trim().length()>0){
                map.put(parse1.getString("agentid"),parse1.getString("position")+parse1.getString("devicename"));
            }
        }
    }

    public String sessionID(String domain,String username,String password,String vmsname) {

        String sessionIDurl = "http://" + domain + "/AdvStreamingService/Authority/Online";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        String str = "{\"request\":{\"username\": \""+username+"\", \"password\":\""+password+"\"}}";
        JSONObject jsonObject = JSONObject.parseObject(str);
        //json对象转Map
        Map<String, Object> requestBody = (Map<String, Object>) jsonObject;
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        //post
        ResponseEntity<String> responseEntity=null;
        restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        try {
            responseEntity =  restTemplate.exchange(sessionIDurl, HttpMethod.PUT, requestEntity, String.class);

        } catch (Exception e) {
            System.err.println(vmsname+"获取sessionID请求出错！");
            System.err.println("错误信息：" + e.getMessage());
            return null;
        }
        JSONObject parse = JSON.parseObject(responseEntity.getBody());
        if (parse.get("result")!=null){
            JSONObject result = (JSONObject) parse.get("result");
            if (result.get("SessionID") != null) {
                System.err.println(vmsname+"获取sessionID成功");
                return result.get("SessionID").toString();
            }else {
                System.err.println(vmsname+"获取sessionID为空");
            }
        }
        return  null;

    }

    public String LiveStream(String domain, String sessionID, String IVSID,String vmsname , String channel) {
        String LiveStreamurl = "http://" + domain + "/AdvStreamingService/LiveStream";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        String str = "{\"request\":{\"method\": \"connection\", \"IVSID\": \"" + IVSID + "\", \"sessionID\": \"" + sessionID + "\", \"channel\": \"" + channel + "\"}}";
        JSONObject jsonObject = JSONObject.parseObject(str);
        //json对象转Map
        Map<String, Object> requestBody = (Map<String, Object>) jsonObject;
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity=null;
        restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        try {
            responseEntity =  restTemplate.exchange(LiveStreamurl, HttpMethod.PUT, requestEntity, String.class);

        } catch (Exception e) {
            System.err.println(vmsname+"   通道："+channel+"   实时Online请求出错！");
            System.err.println("错误信息：" + e.getMessage());
            return null;
        }
        //post
        JSONObject parse = JSON.parseObject(responseEntity.getBody());
        if (parse.get("result")!=null){
            JSONObject result = (JSONObject) parse.get("result");
            if (result.get("mpd") != null) {
                System.err.println(vmsname+"   通道："+channel+"获取实时MPD地址成功");
                return result.get("mpd").toString();
            }else {
                System.err.println(vmsname+"   通道："+channel+"获取实时MPD地址为空");
            }
        }
        return null;
    }

    public String MPD(String mpdurl,String vmsname , String channel,String msg) {
        String url = mpdurl;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.valueOf("application/dash+xml"));
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(null, requestHeaders);
        //post

        ResponseEntity<String> responseEntity=null;
        restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);


        } catch (Exception e) {
            System.err.println(vmsname+"   通道："+channel+ ",  "+msg+"获取mpd文件请求出错！");
            System.err.println("错误信息：" + e.getMessage());
            return null;
        }
        //post
        String body = responseEntity.getBody();
        if (body!=null){
                System.err.println(vmsname+"   通道："+channel+",  "+msg+"获取mpd文件成功！");
                return body;
            }else {
                System.err.println(vmsname+"   通道："+channel+",  "+msg+"获取mpd文件为空！");
        }
        return null;
    }

    public void video(String url,String vmsname , String channel,String msg) {
        HttpHeaders requestHeaders = new HttpHeaders();
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(null, requestHeaders);
        //post
        ResponseEntity<String> responseEntity=null;
        restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        } catch (Exception e) {
            System.err.println(vmsname+"   通道："+channel+ ",  "+msg+"视频请求出错！");
            System.err.println("错误信息：" + e.getMessage());
            return;
        }
        //post
        String body = responseEntity.getBody();
        if (body!=null && body.length()>500){
            System.err.println(vmsname+"   通道："+channel+",  "+msg+"获取视频文件成功！");
            System.out.println(body.length());
        }else {
            System.err.println(vmsname+"   通道："+channel+",  "+msg+"获取视频文件失败！");
        }
    }

    public String PlaybackStream(String domain, String sessionID, String IVSID,String vmsname , String channel) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -1);
        long starttime = now.getTimeInMillis() / 1000;
        long endtime = starttime + 300;


        String LiveStreamurl = "http://" + domain + "/AdvStreamingService/PlaybackStream";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        String str = "{\"request\":{\"method\": \"connection\", \"IVSID\": \"" + IVSID + "\", \"sessionID\": \"" + sessionID + "\", \"channel\": \"" + channel + "\",\"beginTime\": \"" + starttime + "\",\"endTime\": \"" + endtime + "\"}}";
        JSONObject jsonObject = JSONObject.parseObject(str);
        //json对象转Map
        Map<String, Object> requestBody = (Map<String, Object>) jsonObject;
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        //post
        ResponseEntity<String> responseEntity=null;
        restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        try {
           responseEntity = restTemplate.exchange(LiveStreamurl, HttpMethod.PUT, requestEntity, String.class);

        } catch (Exception e) {
            System.err.println(vmsname+"   通道："+channel+"   历史Online请求出错！");
            System.err.println("错误信息：" + e.getMessage());
            return null;
        }
        //post
        JSONObject parse = JSON.parseObject(responseEntity.getBody());
        if (parse.get("result")!=null){
            JSONObject result = (JSONObject) parse.get("result");
            if (result.get("mpd") != null) {
                System.err.println(vmsname+"   通道："+channel+"获取历史MPD地址成功");
                return result.get("mpd").toString();
            }else {
                System.err.println(vmsname+"   通道："+channel+"获取历史MPD地址为空");
            }
        }
        return null;
    }

    public void monite(String domain ,String username,String password,String IVSID,String channelnum,String vmsname,String deviceid) {

        String sessionID = sessionID(domain,username,password,vmsname);
        if ( sessionID==null) return;
        int channel = Integer.valueOf(channelnum) ;
        for (int i = 1; i < channel+1; i++) {
            //实时
            String nowmpdurl = LiveStream(domain, sessionID, IVSID, vmsname,String.valueOf(i));
            if ( nowmpdurl==null) continue;
            nowmpdurl=   nowmpdurl +"?_="+System.currentTimeMillis();
            String video1 = StringUtils.substringBeforeLast(nowmpdurl, "/");
            String mpd = MPD(nowmpdurl,vmsname,String.valueOf(i),"实时");
            if ( mpd==null) continue;
            String tempurl = null;
            try {
                Document document = DocumentHelper.parseText(mpd);
                tempurl = dom4jList(document.getRootElement());
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            String[] split = tempurl.split("&");
            String starturl = video1 + "/" + split[0];
            video(starturl,vmsname,String.valueOf(i),"实时");
            String url1 = video1 + "/" + split[1].replace("$Number$", "1") + "?_=" + System.currentTimeMillis();
            video(url1,vmsname,String.valueOf(i),"实时");


        }

        for (int i = 1; i < channel+1; i++) {
            //历史视频
            String backmpdurl = PlaybackStream(domain, sessionID, IVSID, vmsname,String.valueOf(i));
            String backvideo1 = StringUtils.substringBeforeLast(backmpdurl, "/");
            String backmpd = MPD(backmpdurl,vmsname,String.valueOf(i),"历史");
            String backtempurl = null;
            try {
                Document document = DocumentHelper.parseText(backmpd);
                backtempurl = dom4jList(document.getRootElement());
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            String[] backsplit = backtempurl.split("&");
            String backstarturl = backvideo1 + "/" + backsplit[0];
            video(backstarturl,vmsname,String.valueOf(i),"历史");
            String backurl1 = backvideo1 + "/" + backsplit[1].replace("$Number$", "1") + "?_=" + System.currentTimeMillis();
            video(backurl1,vmsname,String.valueOf(i),"历史");
        }

    }

    @Test
    public  void main() {
        String fanYa = FanYa();
        SheBeiXinxi(fanYa);
        String  token = SSO();
        if ( token==null) return;
        String dashinfo = VCM(token);
        System.out.println("dashinfo = " + dashinfo);
        if ( dashinfo==null) return;
        JSONArray parses = (JSONArray) JSONArray.parse(dashinfo);
        for (Object pars : parses) {
            JSONObject temp = (JSONObject) pars;
            String domain= temp.get("domain")+":"+temp.get("apiport");
            String username = temp.getString("username");
            String password = temp.getString("password");
            String deviceid = temp.getString("deviceid");
            if(map.containsKey(StringUtils.trim(deviceid))){
                System.err.println("********************");
                System.err.println(map.get(StringUtils.trim(deviceid))+":开始检测视频监控");
            }else {
                System.out.println();
                System.err.println(StringUtils.trim(deviceid)+":开始检测视频监控");
            }
            JSONArray dash = (JSONArray) JSONArray.parse(temp.get("vmsinfo").toString());
            for (Object o : dash) {
                JSONObject vms = (JSONObject) o;
                String ivsid = vms.get("ivsid").toString();
                String channelcount = vms.get("channelcount").toString();
                String vmsname = vms.get("vmsname").toString();
                monite(domain,username,password,ivsid,channelcount,vmsname,deviceid);
            }
        }

    }

    public String dom4jList(Element element) {
        String initurl = "";//遍历XML文件
        //获取文件中父元素的名称和文本内容
        if (element.getName().equals("SegmentTemplate")) {
            Attribute genderAttr1 = element.attribute("initialization");

            Attribute genderAttr2 = element.attribute("media");
            initurl = genderAttr1.getValue() + "&" + genderAttr2.getValue();
            return initurl;
        }
        Iterator iterator = element.elementIterator();
        while (iterator.hasNext()) {
            Element e = (Element) iterator.next();
            initurl = dom4jList(e) + initurl;
        }
        return initurl;
    }

//    @Test
//    public void  testes(){
//        AVideoMmonit aVideoMmonit = new AVideoMmonit();
//        aVideoMmonit.monite("http://i201230b25.51mypc.cn:12791","IVS-C4-00-AD-03-65-E4",String.valueOf(1));
//    }
}
