package top.watech.backmonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import top.watech.backmonitor.entity.*;
import top.watech.backmonitor.enums.RespCode;
import top.watech.backmonitor.enums.ResultEnum;
import top.watech.backmonitor.service.UserService;
import top.watech.backmonitor.util.JwtHelper;
import top.watech.backmonitor.util.MatcherUtil;
import top.watech.backmonitor.util.ResultVOUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by fhm on 2018/7/19.
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService ;

    @Autowired
    private Audience audience;

    //登录
    @PostMapping("/")
    public RespEntity login(@RequestBody ReqUser reqUser) throws Exception {

        User login = userService.Login(reqUser.getUserId(), reqUser.getUserPwd());
        if (login != null) {
            String jwtToken = JwtHelper.createJWT(login.getUserName(),
                    login.getUserId(),
                    login.getRole().toString(),
                    audience.getClientId(),
                    audience.getName(),
                    audience.getExpiresSecond()*1000,
                    audience.getBase64Secret());

            String result_str = "bearer;" + jwtToken;

            System.out.println("************************************");
            System.out.println(result_str);
            return new RespEntity(RespCode.SUCCESS, login);
        } else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("用户或密码错误");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }


    @GetMapping("/userList")
    public String getList(){

        return "";
    }


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

    @PostMapping("/login")
    public ResultVo login(@RequestParam(value = "userName", required = true) String userName,
                          @RequestParam(value = "userPwd", required = true) String userPwd,
                          HttpServletRequest request) {

        User query_user = userService.getUserByName(userName);
        if (query_user == null) {
            return ResultVOUtil.error("400", "用户名错误");
        }
        //验证密码
//        PasswordEncoder encoder = new BCryptPasswordEncoder();
//        boolean is_password = encoder.matches(userPwd, query_user.getUserPwd());
//        if (!is_password) {
//            //密码错误，返回提示
//            return ResultVOUtil.error("400", "密码错误");
//        }

        String jwtToken = JwtHelper.createJWT(query_user.getUserName(),
                query_user.getUserId(),
                query_user.getRole().toString(),
                audience.getClientId(),
                audience.getName(),
                audience.getExpiresSecond()*1000,
                audience.getBase64Secret());

        String result_str = "bearer;" + jwtToken;

        System.out.println("************************************");
        System.out.println(result_str);

        return ResultVOUtil.success(result_str);


    }
}
