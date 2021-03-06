package top.watech.backmonitor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Created by wuao.tp on 2018/7/20.
 * RestTemplete配置类
 */
@Configuration
public class RestTemplateConfigurations {
    /**
     * RestTemplete工厂类，生成RestTemplete对象
     * @param factory
     * @return
     */
    @Bean("restTemplate")
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
        RestTemplate restTemplate = new RestTemplate(factory);

        ResponseErrorHandler responseErrorHandler = new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
                return true;
            }
            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
            }
        };
        restTemplate.setErrorHandler(responseErrorHandler);
        return restTemplate;
    }

    /**
     * 设置RestTemplete默认的请求超时时间
     * @return
     */
    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 连接超时,单位为ms
        factory.setReadTimeout(20000);
        // 数据读取超时时间，即SocketTimeout
        factory.setConnectTimeout(20000);
        return factory;
    }


}
