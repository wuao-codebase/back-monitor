package top.watech.backmonitor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import top.watech.backmonitor.entity.PageEntity;
import top.watech.backmonitor.entity.User;
import top.watech.backmonitor.repository.UserRepository;
import top.watech.backmonitor.service.UserService;

import java.util.*;

/**
 * Created by fhm on 2018/7/27.
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public User Login(Long id,String userPwd) throws Exception {
//        User user = new User();
//        user.setUserName("wuao");
//        Example<User> example = Example.of(user);
//        return userRepository.findAll(example);
        return userRepository.getByUserIdIsAndAndUserPwdIs(id,userPwd);
    }
    @Override
    public User getUserById(Long id) throws Exception {
//        User user = new User();
//        user.setUserName("wuao");
//        Example<User> example = Example.of(user);
//        return userRepository.findAll(example);
        return userRepository.findById(id).get();
    }

    @Override
    public PageEntity getUserList(int pageNo,int pageSize) {
        int page = (pageNo < 0) ? 0 : pageNo;
        int size = (pageSize<=0)?20:pageSize;
        Pageable pageable = PageRequest.of(page, size);
        Page<User>  pages = userRepository.findAll(pageable);
        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(pages.getTotalPages());
        pageEntity.setData(pages.getContent());
        pageEntity.setPageNo(pages.getNumber() + 1);
        return pageEntity;
    }

    @Transactional
    public void saveUsers(List< User> users){
        userRepository.saveAll(users);
    }

    @Override
    public User getUserByName(String userName) {
        return userRepository.findByUserName(userName);
    }

//    @Transactional
//    public void updateUsers(Long userId,String username){
//        userRepository.updateUserUsername(userId,username);
//    }

}
