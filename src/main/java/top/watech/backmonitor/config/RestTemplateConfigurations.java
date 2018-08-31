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
      * @Description:   RestTemplete工厂类，生成RestTemplete对象
      * @param:   * @param null
      * @return:
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

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        /**
          * @Description:   设置RestTemplete默认的请求超时时间
          * @param:   * @param
          * @return:
         */
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 连接超时,单位为ms
        factory.setReadTimeout(20000);
        // 数据读取超时时间，即SocketTimeout
        factory.setConnectTimeout(20000);
        return factory;
    }


}
