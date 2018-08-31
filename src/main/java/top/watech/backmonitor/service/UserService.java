package top.watech.backmonitor.service;


import top.watech.backmonitor.entity.ReqUser;
import top.watech.backmonitor.entity.User;

import java.util.List;

/**
 * Created by fhm on 2018/7/23.
 */

public interface UserService {
    /**
     * 登录
     * @param phone
     * @return
     * @throws Exception
     */
    User Login(Long phone) throws Exception;

    /**
     * 根据userId获取用户
     * @param id
     * @return
     * @throws Exception
     */
    User getUserById(Long id) throws Exception;

    /**
     * 获取用户列表，包括用户基本信息和srpName
     * @return
     * @throws Exception
     */
    List<User> getUserList( )throws Exception;

    /**
     * 新增账户
     * @param reqUser
     * @return
     */
    User userInsert(ReqUser reqUser);

    /**
     * 更新账户
     * @param user
     * @return
     */
    User userUpdate(ReqUser user);

    /**
     * 删除一个用户
     * @param Long
     */
    void deleteById(Long Long);

    /**
     * 删除多个用户
     * @param userIDs
     */
    void deleteUserlist(List<Long> userIDs);

    /**
     * 更新用户密码
     * @param userId
     * @param oldPwd
     * @param userPwd
     * @return
     */
    User updateUserpwd(Long userId,String oldPwd, String userPwd);

    /**
     * 重置用户密码
     * @param userId
     * @param userPwd
     * @return
     */
    User upsetUserpwd(Long userId, String userPwd);

    /**
     * 根据srpId获取user列表,包括关联srp信息name等
     * @param srpId
     * @return
     */
    List<User> getUserBySrpId(Long srpId);

    /**
     * 判断当前手机号是否已存在（是否已有对应用户）
     * @param phone
     * @return
     */
    User isPhoneRepet(Long phone);

    /**
     * id不是？且手机号是？的用户
     * @param userId
     * @param phone
     * @return
     */
    User getRepetUser(Long userId,Long phone);
}
