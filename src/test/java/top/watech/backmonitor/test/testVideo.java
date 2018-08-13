package top.watech.backmonitor.test;

/**
 * Created by wuao.tp on 2018/8/13.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jdk.internal.org.xml.sax.InputSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class testVideo {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testdate(){
        System.out.println(  System.currentTimeMillis());
//方法 三
    }

    public String sessionID(String domain) {

        String sessionIDurl ="http://"+ domain+"/AdvStreamingService/Authority/Online";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        String str = "{\"request\":{\"username\": \"admin\", \"password\":\"1234\"}}";
        JSONObject  jsonObject = JSONObject.parseObject(str);
        //json对象转Map
        Map<String,Object> requestBody = (Map<String,Object>)jsonObject;
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        //post
        ResponseEntity<String> responseEntity = restTemplate.exchange(sessionIDurl, HttpMethod.PUT, requestEntity, String.class);
        JSONObject parse = JSON.parseObject(responseEntity.getBody());
        JSONObject result = (JSONObject) parse.get("result");
        return result.get("SessionID").toString();
    }

    public String LiveStream(String domain,String sessionID ,String IVSID,String channel) {
        String LiveStreamurl ="http://"+ domain+"/AdvStreamingService/LiveStream";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        String str = "{\"request\":{\"method\": \"connection\", \"IVSID\": \""+IVSID +"\", \"sessionID\": \""+sessionID+"\", \"channel\": \""+channel+"\"}}";
        JSONObject  jsonObject = JSONObject.parseObject(str);
        //json对象转Map
        Map<String,Object> requestBody = (Map<String,Object>)jsonObject;
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(requestBody, requestHeaders);
        //post
        ResponseEntity<String> responseEntity = restTemplate.exchange(LiveStreamurl, HttpMethod.PUT, requestEntity, String.class);
        JSONObject parse = JSON.parseObject(responseEntity.getBody());
        JSONObject result = (JSONObject) parse.get("result");
        return result.get("mpd").toString();
    }

    public String MPD(String mpdurl) {
        String url =mpdurl;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.valueOf("application/dash+xml"));
        //HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(null, requestHeaders);
        //post
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        return responseEntity.getBody().toString();

    }

    @Test
    public void  main(){
        String domain="i201230b25.51mypc.cn:12791";
        String  IVSID ="IVS-C4-00-AD-03-65-E4";
        String channel="1";
        String sessionID = sessionID(domain);
        System.out.println("sessionID = " + sessionID);
        String mpdurl = LiveStream(domain, sessionID, IVSID, channel)+"?_="+System.currentTimeMillis();
        System.out.println("mpdurl = " + mpdurl);
        String mpd = MPD(mpdurl);
        System.out.println("mpd = " + mpd);
        StringReader sr = new StringReader(mpd);
        InputSource is = new InputSource(sr);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder=factory.newDocumentBuilder();
        Document doc = builder.parse(is);
        strToXml.LoadXml(strXml);

    }

}
