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
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.entity.User;
import top.watech.backmonitor.repository.SrpRepository;
import top.watech.backmonitor.repository.UserRepository;
import top.watech.backmonitor.service.SRPService;

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

    @Autowired
    SRPService srpService;
    @Autowired
    SrpRepository srpRepository;


    @Test
    public void testmtmsave(){
        User user5 = new User();
        SRP srp5 = new SRP();
        SRP srp6 = new SRP();

        user5.setUserName("u005");
        user5.setUserPwd("111111");
        srp5.setSrpName("s005");
        srp6.setSrpName("s006");

        //设置关联关系
        user5.getSrps().add(srp5);
        user5.getSrps().add(srp6);
        srp5.getUsers().add(user5);
        srp6.getUsers().add(user5);

        userRepository.save(user5);
        srpRepository.save(srp5);
        srpRepository.save(srp6);
    }

    /*
     *SRP管理
     */

    //根据userId获取SRPname
    @Test
    public void testFindSrps(){
        List<SRP> srpList = srpService.findByUserId("10000032");
        for (SRP srp : srpList){
            System.err.println(srp.getSrpName());
        }
    }

    //获取所有SRP列表
    @Test
    public void testAllSrps(){
        List<SRP> srpList = srpRepository.findAll();
        for (SRP srp : srpList){
            System.out.println(srp.getSrpId()+":"+srp.getSrpName());
        }
    }

    /*
     *用户管理
     */

    //获取所有用户列表
    @Test
    public void testAllUsers(){
        List<User> userList = userRepository.findAll();
        for (User user:userList){
            System.out.println(user.getUserId()+":"+user.getUserName());
        }
    }

    public void testUserInfo(){

    }
}
