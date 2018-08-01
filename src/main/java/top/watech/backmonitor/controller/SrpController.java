package top.watech.backmonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.watech.backmonitor.entity.RespEntity;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.enums.RespCode;
import top.watech.backmonitor.repository.SrpRepository;
import top.watech.backmonitor.service.SRPService;

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

    /*SRP新增*/
    @PostMapping("/srpInsert")
    public RespEntity srpInsert(@RequestBody SRP srp){
        SRP srp1 = srpService.srpInsert(srp);

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
    @PutMapping ("/srpUpdate")
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

    /*删除一个SRP*/
    @DeleteMapping("/delSrpById")
    public RespEntity deleteSrpById(@RequestParam("srpId") Long srpId){
        srpService.deleteById(srpId);

        if(srpRepository.findBySrpId(srpId)==null){
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
    @DeleteMapping("/delSrpList")
    public RespEntity deleteUserlist(@RequestParam("srpIds") List<Long> srpIds){
        srpService.deleteSrplist(srpIds);

        RespCode respCode = RespCode.WARN;
        respCode.setMsg("删除SRP失败");
        respCode.setCode(-1);
        return new RespEntity(respCode);
    }

}
