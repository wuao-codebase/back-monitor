package top.watech.backmonitor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.entity.User;
import top.watech.backmonitor.repository.SrpRepository;
import top.watech.backmonitor.repository.UserRepository;
import top.watech.backmonitor.service.SRPService;
import top.watech.backmonitor.service.UserService;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fhm on 2018/7/27.
 */

@Service
public class SRPServiceImpl implements SRPService {
    @Autowired
    SrpRepository srpRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;



    /*获取SRP列表*/
    @Override
    public List<SRP> getsrpList() {
        List<SRP> srpList = srpRepository.findAll();
        return srpList;
    }

    //根据userId获取SRP列表
    @Override
    public List<SRP> findByUserId(Long userId) {
        List<SRP> srpList = srpRepository.findAll(new Specification<SRP>() {
            public Predicate toPredicate(Root<SRP> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join<SRP, User> userJoin = root.join("users", JoinType.LEFT);
                return cb.equal(userJoin.get("userId"), userId);
            }
        });
        return srpList;
    }

    /*显示用户列表*/
    @Override
    public List<User> getUserList() {
        ArrayList<User> users = new ArrayList<>();
        List<Object[]> userlist = srpRepository.getusers();
        for (Object[] objects : userlist) {
            User user = new User();
            user.setUserId(Long.valueOf(String.valueOf(objects[0])));
            user.setRole((Integer)objects[2]);
            user.setPhone((String)objects[3]);
            user.setUserPwd((String)objects[4]);
            user.setSrpnames(String.valueOf(objects[6]));
            user.setUserName(String.valueOf(objects[7]));
            users.add(user);
        }
        return users;
    }

    /*根据srpId获取SRP*/
    @Override
    public SRP getSrpById(Long srpId) {
        return srpRepository.findBySrpId(srpId);
    }

    /*SRP新增*/
    @Transactional
    public SRP srpInsert(SRP srp){
        SRP srp1 = new SRP();
        srp1.setSrpName(srp.getSrpName());
        srp1.setDescription(srp.getDescription());
        srp1.setSwitchs(srp.isSwitchs());
        srp1.setFreq(srp.getFreq());

        SRP save = srpRepository.save(srp1);
        return save;
    }

    /*srp更新*/
    @Transactional
    public SRP srpUpdate(SRP srp){
        SRP srp1 = srpRepository.findBySrpId(srp.getSrpId());
        if(srp1!=null){
            srp1.setSrpName(srp.getSrpName());
            srp1.setDescription(srp.getDescription());
            srp1.setSwitchs(srp.isSwitchs());
            srp1.setFreq(srp.getFreq());
            return srpRepository.saveAndFlush(srp1);
        }
        return null;
    }

    /*给SRP加所属用户*/
    @Transactional
    @Override
    public int userAdd(Long srpId, List<Long> userIds) {
        SRP srp = srpRepository.findBySrpId(srpId);
        int oldsize = srp.getUsers().size();
        for (Long userId : userIds){
            User user = userRepository.findByUserId(userId);
            srp.getUsers().add(user);
            user.getSrps().add(srp);
            userRepository.save(user);
        }
        srpRepository.save(srp);
        int addCount = srp.getUsers().size()-oldsize;
        return addCount;
    }

    /*给SRP减所属用户*/
    @Transactional
    @Override
    public int userSub(Long srpId, Long userId) {
        SRP srp = srpRepository.findBySrpId(srpId);
        User user1 = userRepository.findByUserId(userId);

        int oldsize = srp.getUsers().size();

        user1.getSrps().remove(srp);
        srp.getUsers().remove(user1);

        userRepository.save(user1);
        srpRepository.save(srp);

        int addCount = srp.getUsers().size()-oldsize;
        return addCount;
    }

    /*删除一个srp*/
    @Transactional
    @Override
    public void deleteById(Long srpId) {
        SRP srp = srpRepository.findBySrpId(srpId);
        if (srp!=null) {
            List<User> users = userService.getUserBySrpId(srpId);
            for (User user : users){
                user.getSrps().remove(srp);
                srp.getUsers().clear();
                userRepository.saveAll(users);
            }
            srpRepository.save(srp);

            srpRepository.deleteById(srpId);
        }
    }

    /*删除多个srp*/
    @Transactional
    @Override
    public void deleteSrplist(List<Long> srpIDs) {
        for (Long srpId : srpIDs){
            deleteById(srpId);
        }
    }


}
