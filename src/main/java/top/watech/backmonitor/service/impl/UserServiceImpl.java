package top.watech.backmonitor.service.impl;

import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.watech.backmonitor.entity.ReqUser;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.entity.User;
import top.watech.backmonitor.repository.UserRepository;
import top.watech.backmonitor.service.UserService;
import top.watech.backmonitor.util.SecurityUtil;

import javax.persistence.criteria.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fhm on 2018/7/27.
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    /*登录，根据userId和用户密码精确匹配*/
    @Override
    public User Login(Long id,String userPwd) throws Exception {

        return userRepository.getByUserIdIsAndAndUserPwdIs(id,SecurityUtil.md5(userPwd));
    }

    /*根据userId获取用户*/
    @Override
    public User getUserById(Long id) throws Exception {
        return userRepository.findById(id).get();
    }

    //wu获取用户列表，包括用户基本信息和srpName
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
            user.setUserName(String.valueOf(objects[7]));
            users.add(user);
        }
        return users;
    }

    /*用户新增*/
    @Transactional
    @Override
    public User userInsert(ReqUser reqUser) {
        System.err.println(reqUser);
        User user = new User();
        user.setUserName(reqUser.getUserName());
        user.setEmail(reqUser.getEmail());
        user.setPhone(reqUser.getPhone());
        user.setRole(reqUser.getRole());
        try {
            user.setUserPwd(SecurityUtil.md5(reqUser.getUserPwd()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        user.setRemark(reqUser.getRemark());
        User save = userRepository.save(user);
        return save;
    }

    /*用户更新*/
    @Transactional
    @Override
    public User userUpdate(ReqUser user) {
        User user1 = userRepository.findByUserId(user.getUserId());
        if (user1!=null){
            user1.setUserName(user.getUserName());
            user1.setPhone(user.getPhone());
            user1.setEmail(user.getEmail());
            user1.setRemark(user.getRemark());
            if(user.getUserPwd()!=null){
                try {
                    user1.setUserPwd(SecurityUtil.md5(user.getUserPwd()));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
            return userRepository.saveAndFlush(user1);
        }
       else {
            return null;
        }
    }

    /*更新用户密码*/
    @Transactional
    @Override
    public User updateUserpwd(Long userId,String oldPwd, String userPwd) {
        User user1 = userRepository.findByUserId(userId);
        if(userPwd!=null && !"".equals(userPwd)) { //如果没有输入密码，则不修改
            try {
                if(user1!=null && SecurityUtil.md5(user1.getUserName(), oldPwd).equals(user1.getUserPwd())) {
                    user1.setUserId(userId);
                    user1.setUserPwd(SecurityUtil.md5(user1.getUserName(),userPwd));
                    User user = userRepository.saveAndFlush(user1);
                    return user;
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /*删除一个用户*/
    @Transactional
    @Override
    public void deleteById(Long aLong) {
        if (userRepository.findByUserId(aLong)!=null)
            userRepository.deleteById(aLong);
    }

    /*删多个用户*/
    @Transactional
    @Override
    public void deleteUserlist(List<Long> userIDs) {
        for (Long userid : userIDs) {
            if (userRepository.findByUserId(userid)!=null)
                userRepository.deleteById(userid);
        }
    }

    //TODO
    // 方法参数
    /*根据srpId获取user列表(查srp的用户列表时)*/
    @Override
    public List<User> getUserBySrpId(Long srpId) {
        List<User> users = userRepository.findAll(new Specification<User>() {
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join<SRP, User> userJoin = root.join("srps", JoinType.LEFT);
                return cb.equal(userJoin.get("srpId"), srpId);
            }
        });
        return users;
    }

    //保存所有用户
    @Transactional
    public void saveUsers(List< User> users){
        userRepository.saveAll(users);
    }

    //根据用户名获取用户
    @Override
    public User getUserByName(String userName) {
        return userRepository.findByUserName(userName);
    }

//    @Transactional
//    public void updateUsers(Long userId,String username){
//        userRepository.updateUserUsername(userId,username);
//    }

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

//        @Override
//    public List<User> getUserList() {
//            return userRepository.findAll();
//    }

    //更新密码原版
//        if (user1!=null){
//            user1.setUserId(user1.getUserId());
//            user1.setUserPwd(userPwd);
//        }
//        User user = userRepository.saveAndFlush(user1);
//        return user;
}
