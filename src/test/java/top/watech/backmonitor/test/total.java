//package top.watech.backmonitor.test;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.web.client.RestTemplate;
//import top.watech.backmonitor.entity.SRP;
//import top.watech.backmonitor.entity.User;
//import top.watech.backmonitor.repository.SrpRepository;
//import top.watech.backmonitor.repository.TotalReportRepository;
//import top.watech.backmonitor.repository.UserRepository;
//
//import javax.transaction.Transactional;
//import java.util.Set;
//
///**
// * Created by wuao.tp on 2018/8/27.
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@EnableAutoConfiguration
//public class total {
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Autowired
//    private TotalReportRepository totalReportRepository;
//    @Autowired
//    private SrpRepository srpRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//    //无参get请求
//    @Test
//    @Transactional
//    public void get(){
//
//        User byUserId = userRepository.findByUserId(50L);
//        Set<SRP> srps = byUserId.getSrps();
//        for (SRP srp : srps) {
//            System.out.println(srp.getSrpName());
//        }
//    }
//
//
//    }
//
