package top.watech.backmonitor.service;


import top.watech.backmonitor.entity.PageEntity;
import top.watech.backmonitor.entity.User;

import java.util.List;

/**
 * Created by fhm on 2018/7/23.
 */

public interface UserService {
    User Login(Long id,String userPwd) throws Exception;

    User getUserById(Long id) throws Exception;
    PageEntity getUserList(int pageNo, int pageSize)throws Exception;
    void saveUsers(List<User> users) throws Exception;

    User getUserByName(String userName);
}
