package top.watech.backmonitor.service;


import top.watech.backmonitor.entity.ReqUser;
import top.watech.backmonitor.entity.User;

import java.util.List;

/**
 * Created by fhm on 2018/7/23.
 */

public interface UserService {
    User Login(Long id,String userPwd) throws Exception;

    User getUserById(Long id) throws Exception;
//    PageEntity getUserList(int pageNo, int pageSize)throws Exception;
    List<User> getUserList( )throws Exception;
    void saveUsers(List<User> users) throws Exception;

    User getUserByName(String userName);

    //获取所有用户信息，包括关联srp信息name等
    List<User> getAllUserInfo(String srpId);



    //新增账户
    User userInsert(ReqUser reqUser);

    //更新账户
    User userUpdate(User user);

    /*删除一个用户*/
    void deleteById(Long Long);

    /*删除多个用户*/
    void deleteUserlist(List<Long> userIDs);

    /*更新用户密码*/
    User updateUserpwd(Long userId,String userPwd);
}
