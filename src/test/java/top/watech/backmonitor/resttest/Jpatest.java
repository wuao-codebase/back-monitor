package top.watech.backmonitor.resttest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import top.watech.backmonitor.entity.User;
import top.watech.backmonitor.repository.UserRepository;

import java.util.List;

/**
 * Created by wuao.tp on 2018/7/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class Jpatest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void getUserList() {
        List<User> all = userRepository.findAll();
        for (User user : all) {
            System.out.println("user = " +user.getUserName());
        }
    }

    @Test
    public void getUserListByUsername() {
        User user = new User();
        user.setUserName("wuao");
        Example<User> example = Example.of(user);
        List<User> all = userRepository.findAll(example);
        for (User user1 : all) {
            System.out.println("user1 = " + user1.getUserName());
        }
//        System.out.println(userRepository.findById((long) 111).isPresent());

    }



    @Test
    public void insertUser() {
        User user = new User();
        user.setEmail("130278228@qq.com");
        user.setUserName("wuao");
        user.setUserPwd("123456");
        User save = userRepository.save(user);
        System.out.println(save);
    }
        @Test
        public void testPage(){

        int pageNo = 0;
        int pageSize = 5;

//            pageNo = (pageNo==null || pageNo<0)?0:pageNo;
//            pageSize = (pageSize==null || pageSize<=0)?10:pageSize;
            Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<User> page = userRepository.findAll(pageable);

        System.out.println("总记录数: " + page.getTotalElements());
        System.out.println("当前第几页: " + (page.getNumber() + 1));
        System.out.println("总页数: " + page.getTotalPages());
        System.out.println("当前页面的 List: " + page.getContent());
        System.out.println("当前页面的记录数: " + page.getNumberOfElements());
    }

}
