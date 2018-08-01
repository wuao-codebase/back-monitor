package top.watech.backmonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.watech.backmonitor.entity.*;
import top.watech.backmonitor.enums.RespCode;
import top.watech.backmonitor.repository.UserRepository;
import top.watech.backmonitor.service.UserService;
import top.watech.backmonitor.util.JwtHelper;
import top.watech.backmonitor.util.ResultVOUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by fhm on 2018/7/19.
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService ;
    @Autowired
    UserRepository userRepository;

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

            String result_str = "bearer " + jwtToken;

            login.setToken(result_str);
            return new RespEntity(RespCode.SUCCESS, login);
        } else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("用户或密码错误");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
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

    /*根据id取用户*/
    @GetMapping("/getUserById")
    public RespEntity getUserById(@RequestParam("userId") Long userId) throws Exception {
        User userById = userService.getUserById(userId);
        if (userById!=null){
            return new RespEntity(RespCode.SUCCESS,userById);
        }
        else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("获取用户失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /*获取用户列表(admin)*/
    @GetMapping("/userList")
    public RespEntity getList() throws Exception {
        List<User> userList = userService.getUserList();
        if(userList.size()!=0){
            return new RespEntity(RespCode.SUCCESS, userList);
        }
//        System.err.println(userList);
        else{
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("获取用户列表失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /*用户新增*/
    @PostMapping("/userInsert")
    public RespEntity userInsert(@RequestBody User user){
        User user1 = userService.userInsert(user);

        if (user1!=null){
            return new RespEntity(RespCode.SUCCESS,user1);
        }
        else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("添加用户失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /*更新用户信息*/
    @PostMapping("/userUpdate")
    public RespEntity userUpdate(@RequestBody User user){
        User user1 = userService.userUpdate(user.getUserId());

        if (user1!=null){
            return new RespEntity(RespCode.SUCCESS,user1);
        }
        else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("更新用户失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /*更新用户密码*/
    @PostMapping("/updateUserpwd")
    public RespEntity updateUserpwd(@RequestBody ReqUser reqUser){
        User user = userService.updateUserpwd(reqUser.getUserId(), reqUser.getUserPwd());
        if (user!=null){
            return new RespEntity(RespCode.SUCCESS,user);
        }
        else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("重设密码失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /*删除一个用户*/
    @PostMapping("/delUserById")
    public RespEntity deleteUserById(@RequestParam("userId") Long userId){
        userService.deleteById(userId);
        User user = userRepository.findByUserId(userId);
        if(user==null){
            return new RespEntity(RespCode.SUCCESS);
        }
        else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("删除用户失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }



}
