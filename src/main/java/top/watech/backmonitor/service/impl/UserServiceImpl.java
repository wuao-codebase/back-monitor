package top.watech.backmonitor.service.impl;

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
 * UserService接口实现类
 * Created by fhm on 2018/7/27.
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    /*登录，根据手机号匹配*/
    @Override
    public User Login(Long phone) throws Exception {

        return userRepository.findByPhone(phone);
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
            user.setPhone(Long.valueOf(String.valueOf(objects[3])));
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
            try {
                if(SecurityUtil.md5(oldPwd).equals(user1.getUserPwd())) {
                    user1.setUserId(userId);
                    user1.setUserPwd(SecurityUtil.md5(userPwd));
                    User user = userRepository.saveAndFlush(user1);
                    return user;
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        return null;
    }

    /*重置用户密码*/
    @Transactional
    @Override
    public User upsetUserpwd(Long userId, String userPwd) {
        User user = userRepository.findByUserId(userId);
        try {
            if (user!=null){
                user.setUserId(userId);
                user.setUserPwd(SecurityUtil.md5(userPwd));
                User user1 = userRepository.saveAndFlush(user);
                return user1;
            }
        } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        }
        return null;
    }

    /*删除一个用户*/
    @Transactional
    @Override
    public void deleteById(Long userId) {
        User user = userRepository.findByUserId(userId);
        if (user.getRole()!=1){
            userRepository.deleteById(userId);
        }
    }

    /*删多个用户*/
    @Transactional
    @Override
    public void deleteUserlist(List<Long> userIDs) {
        for (Long userid : userIDs) {
            deleteById(userid);
        }
    }

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

    /*判断当前手机号是否已存在（是否已有对应用户）*/
    @Override
    public User isPhoneRepet(Long phone) {
        User user = userRepository.findByPhone(phone);
        return user;
    }

    /*id不是？且手机号是？的用户*/
    @Override
    public User getRepetUser(Long userId, Long phone) {
        User user = userRepository.findByUserIdIsNotAndPhoneIs(userId, phone);
        return user;
    }

    //保存所有用户
    @Transactional
    public void saveUsers(List< User> users){
        userRepository.saveAll(users);
    }
}
