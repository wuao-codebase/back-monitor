package top.watech.backmonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.watech.backmonitor.entity.ReqUser;
import top.watech.backmonitor.entity.RespEntity;
import top.watech.backmonitor.entity.User;
import top.watech.backmonitor.enums.RespCode;
import top.watech.backmonitor.service.UserService;

/**
 * Created by fhm on 2018/7/19.
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService ;

    @PostMapping("/")
    public RespEntity login(@RequestBody ReqUser reqUser) throws Exception {

        User login = userService.Login(reqUser.getUserId(), reqUser.getUserPwd());
        if (login != null) {
            return new RespEntity(RespCode.SUCCESS, login);
        } else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("用户或密码错误");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }
//
//    @PostMapping("/user/userlist")
//    public RespEntity login(@RequestBody ReqUser reqUser) throws Exception {
//
//        User login = userService.Login(reqUser.getUserId(), reqUser.getUserPwd());
//        if (login != null) {
//            return new RespEntity(RespCode.SUCCESS, login);
//        } else {
//            RespCode respCode = RespCode.WARN;
//            respCode.setMsg("用户或密码错误");
//            respCode.setCode(-1);
//            return new RespEntity(respCode);
//        }
//    }

}
