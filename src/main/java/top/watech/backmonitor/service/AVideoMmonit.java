package top.watech.backmonitor.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.watech.backmonitor.entity.DetailReport;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wuao.tp on 2018/8/21.
 */
@Service
public class AVideoMmonit {
    @Autowired
    private  RestTemplate restTemplate;

    private DetailReport detailReport = new DetailReport();
    private JSONObject jsonBody= new JSONObject();
    private  StringBuffer msg =new StringBuffer();
    private boolean codes=true;

    public String sessionID(String domain, String username, String password) {
        String sessionIDurl = domain + "/AdvStreamingService/Authority/Online";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        String str = "{\"request\":{\"username\": \"" + username + "\", \"password\":\"" + password + "\"}}";
        JSONObject jsonObject = JSONObject.parseObject(str);
        //json对象转Map
        Map<String, Object> requestBody = (Map<String, Object>) jsonObject;
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        //post
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(sessionIDurl, HttpMethod.PUT, requestEntity, String.class);

        } catch (Exception e) {
            msg.append("获取sessionID请求出错\n");
            jsonBody.put("Online接口", e.getMessage());
            return null;
        }
        JSONObject parse = JSON.parseObject(responseEntity.getBody());
        if (parse.containsKey("result")&& parse.getString("result").length()>0
                &&parse.getJSONObject("result").containsKey("SessionID")
                && parse.getJSONObject("result").getString("SessionID").length()>0
        ) {
//            msg.append("获取sessionID成功\n");
            jsonBody.put("Online接口", parse);
            return  parse.getJSONObject("result").getString("SessionID");
        }
        msg.append("获取sessionID失败\n");
        jsonBody.put("Online接口", parse);
        return null;

    }

    public String LiveStream(String domain, String sessionID, String IVSID, String channel) {
        String LiveStreamurl = domain + "/AdvStreamingService/LiveStream";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        String str = "{\"request\":{\"method\": \"connection\", \"IVSID\": \"" + IVSID + "\", \"sessionID\": \"" + sessionID + "\", \"channel\": \"" + channel + "\"}}";
        JSONObject jsonObject = JSONObject.parseObject(str);
        //json对象转Map
        Map<String, Object> requestBody = (Map<String, Object>) jsonObject;
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(LiveStreamurl, HttpMethod.PUT, requestEntity, String.class);
        } catch (Exception e) {
            msg.append("实时LiveStream接口请求出错 \n");
            jsonBody.put("LiveStream接口", e.getMessage());
            return null;
        }
        JSONObject parse = JSON.parseObject(responseEntity.getBody());
        if (parse.containsKey("result")&& parse.getString("result").length()>0
                &&parse.getJSONObject("result").containsKey("mpd")
                && parse.getJSONObject("result").getString("mpd").length()>0) {
//            msg.append("获取实时MPD地址成功\n");
            jsonBody.put("LiveStream接口", parse);
            return  parse.getJSONObject("result").getString("mpd");
        }
        msg.append("获取实时MPD地址失败\n");
        jsonBody.put("LiveStream接口", parse);
        return null;
    }

    public String MPD(String mpdurl, String channel, String timemsg) {
        String url = mpdurl;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.valueOf("application/dash+xml"));
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(null, requestHeaders);
        //post
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        } catch (Exception e) {
            msg.append("获取"+timemsg+"mpd文件接口请求出错 \n");
            System.err.println(e.getMessage());
            System.err.println(e);

            jsonBody.put("获取"+timemsg+"mpd文件接口", String.valueOf(e.getMessage()));
            return null;
        }
        String body = responseEntity.getBody();
        if (body != null) {
            String backtempurl = "";
            try {
                Document document = DocumentHelper.parseText(body);
                backtempurl = dom4jList(document.getRootElement());
            } catch (DocumentException e) {
                msg.append("解析"+timemsg+"视频地址失败 \n");
                jsonBody.put("获取"+timemsg+"mpd文件接口", body);
                return null;
            }
            if(backtempurl.length()>0){
//                msg.append("解析"+timemsg+"视频地址成功 \n");
                jsonBody.put("获取"+timemsg+"mpd文件接口", body);
                return backtempurl;
            }else {
                msg.append("解析"+timemsg+"视频地址失败 \n");
                jsonBody.put("获取"+timemsg+"mpd文件接口", body);
                return null;
            }
        } else {
            msg.append("获取"+timemsg+"mpd文件失败 \n");
            jsonBody.put("获取"+timemsg+"mpd文件接口", body);
            return null;
        }
    }

    public void video(String url, String channel, String timemsg) {
        HttpHeaders requestHeaders = new HttpHeaders();
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(null, requestHeaders);
        //post
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        } catch (Exception e) {
            msg.append("获取"+timemsg+"视频接口请求出错 \n");
            jsonBody.put("获取"+timemsg+"视频文件接口", e.getMessage());
            codes=false;
            return;
        }
        String body = responseEntity.getBody();
        if (body != null && body.length() > 600) {
//            msg.append("获取"+timemsg+"视频文件成功 \n");
            jsonBody.put(timemsg+"视频文件接口返回信息","成功");
        } else {
            msg.append("获取"+timemsg+"视频文件失败 \n");
            jsonBody.put(timemsg+"视频文件接口返回信息","成功");
            codes=false;
        }
    }

    public String PlaybackStream(String domain, String sessionID, String IVSID, String channel) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -1);
        long starttime = now.getTimeInMillis() / 1000;
        long endtime = starttime + 300;

        String LiveStreamurl = domain + "/AdvStreamingService/PlaybackStream";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        String str = "{\"request\":{\"method\": \"connection\", \"IVSID\": \"" + IVSID + "\", \"sessionID\": \"" + sessionID + "\", \"channel\": \"" + channel + "\",\"beginTime\": \"" + starttime + "\",\"endTime\": \"" + endtime + "\"}}";
        JSONObject jsonObject = JSONObject.parseObject(str);
        //json对象转Map
        Map<String, Object> requestBody = (Map<String, Object>) jsonObject;
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        //post
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(LiveStreamurl, HttpMethod.PUT, requestEntity, String.class);

        } catch (Exception e) {
            msg.append("历史backStream接口请求出错 \n");
            jsonBody.put("backStream接口", e.getMessage());
            return null;
        }
        JSONObject parse = JSON.parseObject(responseEntity.getBody());
        if (parse.containsKey("result")&& parse.getString("result").length()>0
                &&parse.getJSONObject("result").containsKey("mpd")
                && parse.getJSONObject("result").getString("mpd").length()>0
           ) {
//            msg.append("获取历史MPD地址成功\n");
            jsonBody.put("backStream接口", parse);
            return  parse.getJSONObject("result").getString("mpd");
        }
        msg.append("获取历史MPD地址失败\n");
        jsonBody.put("backStream接口", parse);
        return null;
    }

    //PMD文件解析
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

    //实时
    public void newtime(String domain, String sessionID, String IVSID, String channel) {
        String nowmpdurl = LiveStream(domain, sessionID, IVSID, channel);
        if (nowmpdurl == null) {codes=false; return;}
        nowmpdurl = nowmpdurl + "?_=" + System.currentTimeMillis();
        String video1 = StringUtils.substringBeforeLast(nowmpdurl, "/");
        String mpd = MPD(nowmpdurl, channel, "实时");
        if (mpd == null) {codes=false; return;}
        String[] split = mpd.split("&");
        String starturl = video1 + "/" + split[0];
        video(starturl, channel, "实时");
        String url1 = video1 + "/" + split[1].replace("$Number$", "1") + "?_=" + System.currentTimeMillis();
        video(url1, channel, "实时");
    }

    //历史
    public void blacktime(String domain, String sessionID, String IVSID, String channel) {
        //历史视频
        String backmpdurl = PlaybackStream(domain, sessionID, IVSID, channel);
        if (backmpdurl == null) {codes=false; return;}
        String backvideo1 = StringUtils.substringBeforeLast(backmpdurl, "/");
        String backmpd = MPD(backmpdurl, channel, "历史");
        if (backmpd == null) {codes=false; return;}
        String[] backsplit = backmpd.split("&");
        String backstarturl = backvideo1 + "/" + backsplit[0];
        video(backstarturl, channel, "历史");
        String backurl1 = backvideo1 + "/" + backsplit[1].replace("$Number$", "1") + "?_=" + System.currentTimeMillis();
        video(backurl1, channel, "历史");
    }

    public DetailReport monite(String domain, String IVSID, String channel) {
        String sessionID = sessionID(domain, "admin", "1234");
        if (sessionID != null) {
            newtime(domain, sessionID, IVSID, channel);
            blacktime(domain, sessionID, IVSID, channel);}else {
            codes=false;
        }
            detailReport.setCode(codes);
        String tempstring="";
        if(!codes){
             tempstring =  msg.substring(0,msg.length()-1);
        }
        detailReport.setMessage(tempstring);
        detailReport.setMessageBody(String.valueOf(jsonBody));
        msg.setLength(0);
        codes=true;
        jsonBody=new JSONObject();
        return detailReport;
    }
}
