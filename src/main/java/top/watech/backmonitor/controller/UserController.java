package top.watech.backmonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.watech.backmonitor.entity.Audience;
import top.watech.backmonitor.entity.ReqUser;
import top.watech.backmonitor.entity.RespEntity;
import top.watech.backmonitor.entity.User;
import top.watech.backmonitor.enums.RespCode;
import top.watech.backmonitor.repository.UserRepository;
import top.watech.backmonitor.service.UserService;
import top.watech.backmonitor.util.JwtHelper;
import top.watech.backmonitor.util.SecurityUtil;

import java.util.List;

/**
 * Created by fhm on 2018/7/19.
 * @Description:监控项相关的crud接口
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService ;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private Audience audience;

    /**
     * 登录
     * @param reqUser
     * @return
     * @throws Exception
     */
    @PostMapping("/userlogin")
    public RespEntity login(@RequestBody ReqUser reqUser) throws Exception {
        User login = userService.Login(reqUser.getPhone());

        if(login==null )
        {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("用户不存在");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
        else if (!SecurityUtil.md5(reqUser.getUserPwd()).equals(login.getUserPwd())){
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("密码错误");
            respCode.setCode(-2);
            return new RespEntity(respCode);
        }
        else {
            String jwtToken = JwtHelper.createJWT(login.getUserName(),
                    login.getUserId(),
                    login.getRole().toString(),
                    audience.getClientId(),
                    audience.getName(),
                    audience.getExpiresSecond()*1000*60*20,
                    audience.getBase64Secret());

            String result_str = "bearer " + jwtToken;

            login.setToken(result_str);//登录成功生成token
            return new RespEntity(RespCode.SUCCESS, login);
        }
    }

    /**
     * 根据id取用户
     * @param userId
     * @return
     * @throws Exception
     */
    @GetMapping("/filter/getUserById")
    public RespEntity getUserById(@RequestParam("userId") Long userId) throws Exception {
        User userById = userService.getUserById(userId);
        if (userById!=null){
            return new RespEntity(RespCode.SUCCESS,userById);
        }
        else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("根据userId获取用户失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /**
     * 获取用户列表(admin)
     * @return 用户列表
     * @throws Exception
     */
    @GetMapping("/filter/userList")
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

    /**
     * 用户新增
     * @param reqUser
     * @return
     */
    @PostMapping("/filter/userInsert")
    public RespEntity userInsert(@RequestBody  ReqUser reqUser){
        User user1 = userService.userInsert(reqUser);
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

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @PutMapping("/filter/userUpdate")
    public RespEntity userUpdate(@RequestBody ReqUser user){
        System.err.println(user);
        User user1 = userService.userUpdate(user);

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

    /**
     * id不是？且手机号是？的用户
     * @param userId
     * @param phone
     * @return
     * @throws Exception
     */
    @GetMapping("/filter/getRepetUser/{userId}/{phone}")
    public RespEntity getRepetUser(@PathVariable Long userId,@PathVariable Long phone) throws Exception {
//        public RespEntity getRepetUser(@RequestParam("userId") Long userId,@RequestParam("phone") Long phone) throws Exception {
        User repetUser = userService.getRepetUser(userId, phone);
        if (repetUser!=null){
            return new RespEntity(RespCode.SUCCESS,repetUser);
        }
        else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("没有重复用户");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /**
     * 更新用户密码
     * @param UserId
     * @param oldPwd
     * @param userPwd
     * @return
     */
    @PutMapping ("/filter/updateUserpwd/{UserId}/{oldPwd}/{userPwd}")
    public RespEntity updateUserpwd(@PathVariable Long  UserId, @PathVariable String oldPwd, @PathVariable String userPwd){
        System.err.println(UserId+oldPwd+userPwd);
        User user = userService.updateUserpwd(UserId, oldPwd, userPwd);
        if (user!=null){
            return new RespEntity(RespCode.SUCCESS,user);
        }
        else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("原始密码输入错误");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /**
     * 重置用户密码
     * @param userId
     * @param userPwd
     * @return
     */
    @PostMapping ("/filter/upsetUserpwd/{userId}/{userPwd}")
    public RespEntity upsetUserpwd(@PathVariable Long userId, @PathVariable String userPwd){
        User user1 = userService.upsetUserpwd(userId, userPwd);
        return new RespEntity(RespCode.SUCCESS,user1);
    }

    /**
     * 删除一个用户
     * @param userId
     * @return
     */
    @DeleteMapping("/filter/delUserById")
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

    /**
     * 删除多个用户
     * @param userIDs
     * @return
     */
    @DeleteMapping("/filter/delUserList/{userIDs}")
    public RespEntity deleteUserlist(@PathVariable List<Long> userIDs){
        userService.deleteUserlist(userIDs);
        return new RespEntity(RespCode.SUCCESS);
    }

    /**
     * 根据srpId获取user列表
     * @param SRPID
     * @return
     */
    @GetMapping("/filter/getUserBySrpId/{SRPID}")
    public RespEntity getUserBySrpId(@PathVariable Long SRPID){
        List<User> users = userService.getUserBySrpId(SRPID);

        if (users!=null){
            return new RespEntity(RespCode.SUCCESS,users);
        }
        else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("根据srpId获取用户失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /**
     * 判断当前手机号是否已存在（是否已有对应用户）
     * @param phone
     * @return
     */
    @GetMapping("/filter/isPhoneRepet/{phone}")
    public RespEntity isPhoneRepet(@PathVariable Long phone){
        User user = userService.isPhoneRepet(phone);
        if (user==null){
            return new RespEntity(RespCode.SUCCESS);
        }
        else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("此手机号已有人使用");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }
}
