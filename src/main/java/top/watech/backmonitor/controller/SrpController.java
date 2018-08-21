package top.watech.backmonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.watech.backmonitor.entity.MonitorItem;
import top.watech.backmonitor.entity.RespEntity;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.entity.User;
import top.watech.backmonitor.enums.RespCode;
import top.watech.backmonitor.repository.MonitorItemRepository;
import top.watech.backmonitor.repository.SrpRepository;
import top.watech.backmonitor.repository.UserRepository;
import top.watech.backmonitor.service.MonitorItemService;
import top.watech.backmonitor.service.SRPService;
import top.watech.backmonitor.service.UserService;

import java.util.List;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by fhm on 2018/7/19.
 */
@RestController
public class SrpController {

    @Autowired
    SRPService srpService;
    @Autowired
    SrpRepository srpRepository;
    @Autowired
    UserService userService;
    @Autowired
    MonitorItemRepository monitorItemRepository;

    /*获取SRP列表*/
    @PostMapping ("/filter/srpList")
    public RespEntity getsrpList(@RequestBody User user) {
        if (user.getRole()==1){
            List<SRP> srpList = srpService.getsrpList();
            if(srpList!=null){
                return new RespEntity(RespCode.SUCCESS, srpList);
            }
            else{
                RespCode respCode = RespCode.WARN;
                respCode.setMsg("获取SRP列表失败");
                respCode.setCode(-1);
                return new RespEntity(respCode);
            }
        }
        else {
            List<SRP> srpList = srpService.findByUserId(user.getUserId());
            if(srpList!=null){
                return new RespEntity(RespCode.SUCCESS, srpList);
            }
            else{
                RespCode respCode = RespCode.WARN;
                respCode.setMsg("获取SRP列表失败");
                respCode.setCode(-1);
                return new RespEntity(respCode);
            }
        }

    }
    /*根据srpId获取SRP*/
    @GetMapping("/filter/getSrpById")
    public RespEntity getUserById(@RequestParam("srpId") Long srpId) throws Exception {
            SRP srp = srpService.getSrpById(srpId);
        if (srp!=null){
            return new RespEntity(RespCode.SUCCESS,srp);
        }
        else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("根据srpId获取SRP失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /*SRP新增*/
    @PostMapping("/filter/srpInsert/{userIds}")
    public RespEntity srpInsert(@RequestBody SRP srp,@PathVariable List<Long> userIds){
        SRP srp1 = srpService.srpInsert(srp, userIds);

        if (srp1!=null){
            return new RespEntity(RespCode.SUCCESS,srp1);
        }
        else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("添加SRP失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /*更新SRP信息*/
    @PutMapping ("/filter/srpUpdate")
    public RespEntity srpUpdate(@RequestBody SRP srp){
        SRP srp1 = srpService.srpUpdate(srp);

        if (srp1!=null){
            return new RespEntity(RespCode.SUCCESS,srp1);
        }
        else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("更新SRP失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /*给SRP加所属用户*/
    @PostMapping("/filter/userAdd/{srpId}/{userIds}")
    public RespEntity userAdd(@PathVariable Long srpId, @PathVariable List<Long> userIds){
        int userCount = srpService.userAdd(srpId, userIds);
        if (userCount>0){
            return new RespEntity(RespCode.SUCCESS,userCount);
        }
        else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("给SRP添加所属用户失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /*给SRP减所属用户*/
    @DeleteMapping ("/filter/userSub/{srpId}/{userId}")
    public RespEntity userSub(@PathVariable Long srpId, @PathVariable Long userId){
        int userCount = srpService.userSub(srpId,userId);
        if (userCount<0){
            return new RespEntity(RespCode.SUCCESS,userCount);
        }
        else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("给SRP删除用户失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /*删除一个SRP*/
    @DeleteMapping("/filter/delSrpById")
    public RespEntity deleteSrpById(@RequestParam("srpId") Long srpId){
        srpService.deleteById(srpId);

        if(srpRepository.findBySrpIdOrderBySrpId(srpId)==null){
            return new RespEntity(RespCode.SUCCESS);
        }
        else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("删除SRP失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /*删除多个SRP*/
    @DeleteMapping("/filter/delSrpList/{srpIds}")
    public RespEntity deleteUserlist(@PathVariable List<Long> srpIds){
        srpService.deleteSrplist(srpIds);
        return new RespEntity(RespCode.SUCCESS);
    }

    /*显示用户列表（给SRP加用户时选用户）*/
    @GetMapping("/filter/users")
    public RespEntity getAllUsers() throws Exception {
        List<User> users = srpService. getUserList();
        if(users.size()!=0){
            return new RespEntity(RespCode.SUCCESS, users);
        }
        else{
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("显示用户列表失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /*根据srpId获取user列表*/
    @PutMapping("/filter/getUserBySrpId")
    public RespEntity getUserBySrpId(@RequestParam("srpId") Long srpId){
        List<User> users = userService.getUserBySrpId(srpId);

        if (users!=null){
            return new RespEntity(RespCode.SUCCESS,users);
        }
        else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("根据srpId获取用户列表失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /*给SRP加监控项*/
    @PostMapping("/filter/monitorItemAdd")
    public RespEntity monitorItemAdd(@RequestBody MonitorItem monitorItem){
//        SRP srp = srpService.monitorItemAdd(srpId, monitorItem);
        System.err.println(monitorItem);
        SRP srp = srpService.monitorItemAdd(monitorItem);
        System.err.println(srp);
        if (srp!=null){
            return new RespEntity(RespCode.SUCCESS,srp);
        }
        else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("给SRP添加监控项失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /*给SRP减监控项*/
    @DeleteMapping("/filter/monitorItemSub")
    public RespEntity monitorItemSub(@RequestParam("monitorItemId") Long monitorItemId){
        srpService.monitorItemSub(monitorItemId);
        MonitorItem monitorItem = monitorItemRepository.findByMonitorId(monitorItemId);
        if(monitorItem==null){
            return new RespEntity(RespCode.SUCCESS);
        }
        else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("删除监控项失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    //监控项详细配置查看的接口在MonitorItemController文件中
    //@GetMapping("/getMonitorById")

    //SRP下监控项列表查看的接口在MonitorItemController文件中
    //@GetMapping("/getMonitorsBySrpId")
}
