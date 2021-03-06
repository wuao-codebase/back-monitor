package top.watech.backmonitor.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import top.watech.backmonitor.entity.MonitorItem;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.entity.User;
import top.watech.backmonitor.repository.MonitorItemRepository;
import top.watech.backmonitor.repository.SrpRepository;
import top.watech.backmonitor.repository.UserRepository;
import top.watech.backmonitor.service.SRPService;
import top.watech.backmonitor.service.UserService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wuao.tp on 2018/7/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class TestSrp {

    @Autowired
    UserRepository userRepository;
    @Autowired
    SRPService srpService;
    @Autowired
    SrpRepository srpRepository;
    @Autowired
    UserService userService;
    @Autowired
    MonitorItemRepository monitorItemRepository;

    /*
     *SRP管理
     */

    //根据userId获取SRPname(可用在某SRP接收者列表)
    @Test
    public void testFindSrps() {
        List<SRP> srpList = srpService.findByUserId(10000032L);
        for (SRP srp : srpList) {
            System.err.println(srp);
        }
    }

    //根据srpId获取SRPname(可用在某SRP接收者列表)
    @Test
    public void testFindSrps1() {
        SRP srp = srpRepository.findBySrpIdOrderBySrpId(91L);

        System.err.println(srp);

    }

    //根据srpId获取SRP
    @Test
    public void testFindSrps2() {
        SRP srp = srpRepository.findBySrpId(65L);

        System.err.println(srp);

    }

    //获取所有SRP列表
    @Test
    public void testAllSrps() {
        List<SRP> srpList = srpRepository.findAll();
        for (SRP srp : srpList) {
//            System.out.println(srp.getSrpId()+":"+srp.getSrpName());
            System.err.println(srp);
        }
    }

    //SRP新增
    @Test
    public void testSrpInsert() {
        SRP srp = new SRP();
        srp.setSrpName("yayaya");
        srp.setDescription("XXXXXXXXXXyayayaXXXXXXXXXXXXX");
        srp.setSwitchs(true);
        srp.setFreq(20);

        srpRepository.save(srp);
    }

    @Test
    public void testSrpInsert2() {
        for (int i = 1 ; i <= 10 ; i ++){
            SRP srp = new SRP();
            srp.setSrpName("srp"+i);
            srp.setDescription("XXXXXXXXXXyayayaXXXXXXXXXXXXX");
            srp.setSwitchs(true);
            srp.setFreq(20);
            srpRepository.save(srp);
        }
    }

    @Test
    public void testSrpInsert3(){
        SRP srp = new SRP();

        User user1 = userRepository.findByUserId(51L);
        User user2 = userRepository.findByUserId(52L);
        User user3 = userRepository.findByUserId(53L);

        srp.setSrpName("srpsrp3");
        srp.setDescription("XXXXXXXXXXyayayaXXXXXXXXXXXXX");
        srp.setSwitchs(true);
        srp.setFreq(20);

//        user1.getSrps().add(srp);
//        user2.getSrps().add(srp);
//        user3.getSrps().add(srp);
//
//        srp.getUsers().add(user1);
//        srp.getUsers().add(user2);
//        srp.getUsers().add(user3);

        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        srp.setUsers(users);

        user1.getSrps().add(srp);
        user2.getSrps().add(srp);
        user3.getSrps().add(srp);

        userRepository.saveAndFlush(user1);
        userRepository.saveAndFlush(user2);
        userRepository.saveAndFlush(user3);

        srpRepository.save(srp);
    }

    @Test
    public void testSrpInsert4(){
        SRP srp = new SRP();

//        User user1 = new User();
//        user1.setUserName("yyy");
//        user1.setUserPwd("123456");
//        user1.setPhone(13700000001L);

        User user1 = userRepository.findByUserId(59L);

        srp.setSrpName("srpxyz111");
        srp.setDescription("XXXXXXXXXXyayayaXXXXXXXXXXXXX");
        srp.setSwitchs(true);
        srp.setFreq(20);

        user1.getSrps().add(srp);
        srp.getUsers().add(user1);

        userRepository.saveAndFlush(user1);

        srpRepository.save(srp);

        System.err.println(srp);
    }

    //SRP更新
    @Test
    public void testSrpUpdate() {
        SRP srp1 = srpRepository.findBySrpIdOrderBySrpId(18L);

        srp1.setDescription("ddddddddddddddddddd");
        srpRepository.saveAndFlush(srp1);
    }


    //删除一个srp（关系表和srp表会更新，关联user不会被删除,关联的监控项会删掉）
    @Test
    public void testDelSrp4() {
        //        SRP srp = srpRepository.findBySrpId(48L);
        User u1 = userRepository.findByUserId(10000054L);
        SRP srp = u1.getSrps().iterator().next();

        u1.getSrps().remove(srp);
        srp.getUsers().clear();

        srpRepository.saveAndFlush(srp);
        userRepository.saveAndFlush(u1);

        srpRepository.delete(srp);
    }

    //给SRP加所属用户
    @Test
    public void testSrpUserInsert() {
        User u1 = userRepository.findByUserId(111L);
//        User u2 = userRepository.findByUserId(10000054L);

        SRP srpnew = srpRepository.findBySrpIdOrderBySrpId(66L);
        SRP srpnew2 = srpRepository.findBySrpIdOrderBySrpId(65L);

//        srpnew.getUsers().addAll(Arrays.asList(u1, u2));
        srpnew.getUsers().add(u1);
        srpnew2.getUsers().add(u1);
//        srpnew.getUsers().add(u2);
        u1.getSrps().add(srpnew);
        u1.getSrps().add(srpnew2);
//        u2.getSrps().add(srpnew);
//
        srpRepository.saveAndFlush(srpnew);
        srpRepository.saveAndFlush(srpnew2);
        userRepository.saveAndFlush(u1);
//        userRepository.save(u2);
    }

    //给SRP加所属用户
    @Test
    public void testSrpUserInsert2() {
        User u1 = new User();
        u1.setUserName("fanyaXX");
        u1.setUserPwd("123456");
        u1.setPhone(13900000000L);

        SRP srpnew = srpRepository.findBySrpIdOrderBySrpId(66L);

        srpnew.getUsers().add(u1);
        u1.getSrps().add(srpnew);

        srpRepository.save(srpnew);
        userRepository.save(u1);
    }

    //给SRP减所属用户(解除关系)
    @Test
    public void testSrpUserSub3() {
        User u1 = userRepository.findByUserId(111L);
//        SRP srp = srpRepository.findBySrpId(48L);
        SRP srp = u1.getSrps().iterator().next();

        u1.getSrps().remove(srp);
        srp.getUsers().remove(u1);

//        System.err.println(srp);
//        System.err.println(srp.getUsers());

        srpRepository.saveAndFlush(srp);
        userRepository.saveAndFlush(u1);
    }

    //给SRP加监控项
    @Test
    public void testItemAdd() {
        SRP srp = srpRepository.findBySrpIdOrderBySrpId(66L);
        MonitorItem monitorItem3 = new MonitorItem();
        MonitorItem monitorItem2 = new MonitorItem();
//        MonitorItem monitorItem3 = monitorItemRepository.findByMonitorId(1L);
//        MonitorItem monitorItem4 = monitorItemRepository.findByMonitorId(3L);

        monitorItem2.setMonitorName("APIssssssssssss");
        monitorItem2.setRemark("API111111111111");
        monitorItem2.setUrl("http://api-pataciot-acniotsense.wise-paas.com.cn/api/v1.0/authentication/login/phone");
        monitorItem2.setRequestType(1);
        monitorItem2.setRequestBody("{\n" +
                "    \"password\":\"123456\",\n" +
                "    \"phone\":\"13900000000\",\n" +
                "    \"tokenTs\":20160\n" +
                "}\n");
        monitorItem2.setAsserts("{\n" +
                "    \"statusCode\":\"200\"\n" +
                "}");

        monitorItem3.setMonitorName("APIwwwwwwwwwwww");
        monitorItem3.setRemark("AP33333333333333333");
        monitorItem3.setUrl("http://api-pataciot-acniotsense.wise-paas.com.cn/api/v1.0/authentication/login/phone");
        monitorItem3.setRequestType(1);
        monitorItem3.setRequestBody("{\n" +
                "    \"password\":\"123456\",\n" +
                "    \"phone\":\"13900000000\",\n" +
                "    \"tokenTs\":20160\n" +
                "}\n");
        monitorItem3.setAsserts("{\n" +
                "    \"statusCode\":\"200\"\n" +
                "}");

        srp.getMonitorItems().add(monitorItem3);
        srp.getMonitorItems().add(monitorItem2);
        monitorItem3.setSrp(srp);
        monitorItem2.setSrp(srp);

        srpRepository.save(srp);
        monitorItemRepository.save(monitorItem3);
        monitorItemRepository.save(monitorItem2);
    }

    //给SRP减监控项
    @Test
    public void testItemSub() {
        List<MonitorItem> monitorItemList = monitorItemRepository.findBySrpIdOrderByClassify(2L);
        for (MonitorItem monitorItem : monitorItemList){
            monitorItemRepository.delete(monitorItem);
        }
    }

    //给SRP清空监控项
    @Test
    public void testItemClear() {
        SRP srp = srpRepository.findBySrpIdOrderBySrpId(16L);

        srp.getMonitorItems().clear();
        srpRepository.saveAndFlush(srp);

        for (MonitorItem monitorItem : srp.getMonitorItems()){
            monitorItemRepository.delete(monitorItem);
        }
    }

    //SRP监控项列表
    @Test
    public void testMonitorListByclassify(){
        List<MonitorItem> bySrpIdOrderByClassify = monitorItemRepository.findBySrpIdOrderByClassify(1L);
        for (MonitorItem monitorItem : bySrpIdOrderByClassify){
            System.err.println(monitorItem);
        }

    }

    @Test
    public void testPage() {

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
