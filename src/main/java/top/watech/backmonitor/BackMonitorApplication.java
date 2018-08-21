package top.watech.backmonitor;

import org.assertj.core.util.Lists;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import top.watech.backmonitor.util.JwtFilter;

import java.util.List;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class BackMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackMonitorApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtFilter());
        //添加需要拦截的url
        List<String> urlPatterns = Lists.newArrayList();


//        urlPatterns.add("/*");
        urlPatterns.add("/srp123");
//        urlPatterns.add("/");
//        urlPatterns.remove("/");
//        registrationBean.addUrlPatterns(urlPatterns.toArray(new String[urlPatterns.size()]));
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.addInitParameter("exclusions", "/user/*");

//        registrationBean.addInitParameter("login", "/");
        System.err.println(registrationBean.getInitParameters());
        System.err.println(registrationBean.getUrlPatterns());
        return registrationBean;

    }
}
