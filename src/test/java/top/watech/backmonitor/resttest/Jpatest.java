package top.watech.backmonitor.resttest;

import org.junit.After;
import org.junit.Before;
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
import top.watech.backmonitor.service.UserService;

import java.awt.print.Book;
import java.util.Arrays;
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
    @Autowired
    UserService userService;


    //多对多保存
    @Test
    public void testmtmsave(){
        User user7 = new User();
        SRP srp7 = new SRP();
        SRP srp8 = new SRP();

        user7.setUserName("u007");
        user7.setUserPwd("111111");
        srp7.setSrpName("s007");
        srp8.setSrpName("s008");

        //设置关联关系
        user7.getSrps().add(srp7);
        user7.getSrps().add(srp8);
        srp7.getUsers().add(user7);
        srp8.getUsers().add(user7);

        userRepository.save(user7);
        srpRepository.save(srp7);
        srpRepository.save(srp8);
    }

    /*
     *SRP管理
     */

    //根据userId获取SRPname(可用在某SRP接收者列表)
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

    //SRP新增
    @Test
    public void testSrpInsert(){
        SRP srp = new SRP();
        srp.setSrpName("yayaya");
        srp.setDescription("XXXXXXXXXXyayayaXXXXXXXXXXXXX");
        srp.setSwitchs(true);
        srp.setFreq(20);

        srpRepository.save(srp);

    }

    //给SRP加所属用户
    @Test
    public void testSrpUserInsert(){
        User u1 = userRepository.findByUserId(10000032L);
        User u2 = userRepository.findByUserId(10000034L);

        SRP srpnew = srpRepository.findBySrpId(26L);

        System.err.println(u1);
        System.err.println(u2);
        System.err.println(srpnew);

//        srpnew.getUsers().addAll(Arrays.asList(u1,u2));
        srpnew.getUsers().add(u1);
        srpnew.getUsers().add(u2);
        u1.getSrps().add(srpnew);
        u2.getSrps().add(srpnew);
//
        srpRepository.save(srpnew);
        userRepository.save(u1);
        userRepository.save(u2);
    }

//    //删除一个srp（关系表和user表会更新，关联SRP不会被删除）
//    @Test
//    public void testDelSrp(){
//        srpRepository.deleteById(16L);
//    }



    /*
     *用户管理
     */

    //获取所有用户列表
    @Test
    public void testAllUsers(){
        List<User> userList = userRepository.findAll();
        for (User user:userList){
            System.out.println(user);
        }
    }

    //新增账户
    @Test
    public void testUserInsert(){
        User user = new User("user01");
        user.setRole(1);
        user.setPhone("18300000000");
        user.setEmail("xxx@xx.com");
        user.setUserPwd("123456");
        user.setRemark("user01xxx");

        userRepository.save(user);

        System.out.println(user);
    }

    //删除一个用户（关系表和user表会更新，关联SRP不会被删除）
    @Test
    public void testDelUser(){
        userRepository.deleteById(10000034L);
    }

    //更新某用户信息
    @Test
    public void updateUser() {

        User user = userRepository.findByUserId(10000032L);
        if (user!=null){
            userService.updateUserEmail(10000032L,"zhangsan@xx.cn");
        }
    }

    //根据srpId获取user信息
    @Test
    public void testFindUsers(){
        List<User> userList = userService.getAllUserInfo("16");

        for (User user : userList){
            System.err.println(user);
        }
    }
}
