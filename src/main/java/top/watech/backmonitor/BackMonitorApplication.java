package top.watech.backmonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//@EnableScheduling
@EnableJpaAuditing
public class BackMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackMonitorApplication.class, args);
    }
}
