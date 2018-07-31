package top.watech.backmonitor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.entity.User;
import top.watech.backmonitor.repository.UserRepository;
import top.watech.backmonitor.service.UserService;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.ArrayList;

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

    //后端分页
//    @Override
//    public PageEntity getUserList(int pageNo,int pageSize) {
//        int page = (pageNo < 0) ? 0 : pageNo;
//        int size = (pageSize<=0)?25:pageSize;
//        Pageable pageable = PageRequest.of(page, size);
//        Page<User>  pages = userRepository.findAll(pageable);
//        PageEntity pageEntity = new PageEntity();
//        pageEntity.setTotal(pages.getTotalPages());
//        pageEntity.setData(pages.getContent());
//        pageEntity.setPageNo(pages.getNumber() + 1);
//        return pageEntity;
//    }

        @Override
    public List<User> getUserList() {
            return userRepository.findAll();
    }

    //wuao
    @Override
    public List<User> getUserList() {
        ArrayList<User> users = new ArrayList<>();
        List<Object[]> getuserlist = userRepository.getuserlist();
        for (Object[] objects : getuserlist) {
            User user = new User();
            user.setUserId(Long.valueOf(String.valueOf(objects[0])));
            user.setEmail((String)objects[1]);
            user.setRole((Integer)objects[2]);
            user.setPhone((String)objects[3]);
            user.setUserPwd((String)objects[4]);
            user.setRemark((String)objects[5]);
            user.setSrpnames(String.valueOf(objects[6]));
            users.add(user);
        }
        return users;
    }


    @Transactional
    public void saveUsers(List< User> users){
        userRepository.saveAll(users);
    }

    @Override
    public User getUserByName(String userName) {
        return userRepository.findByUserName(userName);
    }


    //根据srpId获取user列表
    @Override
    public List<User> getAllUserInfo(String srpId) {
        List<User> users = userRepository.findAll(new Specification<User>() {
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join<SRP, User> userJoin = root.join("srps", JoinType.LEFT);
                return cb.equal(userJoin.get("srpId"), srpId);
            }
        });
        return users;
    }

    @Transactional
    @Override
    public void updateUserEmail(Long userId, String email) {
        userRepository.updateUserEmail(userId,email);
    }

//    @Transactional
//    public void updateUsers(Long userId,String username){
//        userRepository.updateUserUsername(userId,username);
//    }

}
