package top.watech.backmonitor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.watech.backmonitor.entity.MonitorItem;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.entity.User;
import top.watech.backmonitor.repository.MonitorItemRepository;
import top.watech.backmonitor.repository.SrpRepository;
import top.watech.backmonitor.repository.UserRepository;
import top.watech.backmonitor.service.MonitorItemService;
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
    @Autowired
    MonitorItemRepository monitorItemRepository;
    @Autowired
    MonitorItemService monitorItemService;
    @Autowired
    SRPService srpService;



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
            user.setPhone((Long) objects[3]);
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
        return srpRepository.findBySrpIdOrderBySrpId(srpId);
    }

    /*SRP新增*/
    @Transactional
    public SRP srpInsert(SRP srp,List<Long> userIds){

        SRP save = srpRepository.save(srp);
        for (Long userId : userIds){
            User user = userRepository.findByUserId(userId);
            srp.getUsers().add(user);
            user.getSrps().add(srp);
            userRepository.saveAndFlush(user);
        }
        return save;
    }

    /*srp更新*/
    @Transactional
    public SRP srpUpdate(SRP srp){
        if(srp!=null){
            return srpRepository.saveAndFlush(srp);
        }
        return null;
    }

    /*给SRP加所属用户*/
    @Transactional
    @Override
    public int userAdd(Long srpId, List<Long> userIds) {
        SRP srp = srpRepository.findBySrpIdOrderBySrpId(srpId);
        int oldsize = srp.getUsers().size();
        for (Long userId : userIds){
            User user = userRepository.findByUserId(userId);
            if (!srp.getUsers().contains(user)){
                srp.getUsers().add(user);
                user.getSrps().add(srp);
                userRepository.save(user);

            }
        }
        srpRepository.save(srp);
        int addCount = srp.getUsers().size()-oldsize;
        return addCount;
    }

    /*给SRP减所属用户(解除关系)*/
    @Transactional
    @Override
    public int userSub(Long srpId, Long userId) {
        SRP srp = srpRepository.findBySrpIdOrderBySrpId(srpId);
        User user1 = userRepository.findByUserId(userId);

        int oldsize = srp.getUsers().size();

        user1.getSrps().remove(srp);
        srp.getUsers().remove(user1);

        userRepository.saveAndFlush(user1);
        srpRepository.saveAndFlush(srp);

        int addCount = srp.getUsers().size()-oldsize;
        return addCount;
    }

    /*删除一个srp*/
    @Transactional
    @Override
    public void deleteById(Long srpId) {
        SRP srp = srpRepository.findBySrpIdOrderBySrpId(srpId);
        if (srp!=null) {
            if (srp.getUsers()!=null) {
                List<User> users = userService.getUserBySrpId(srpId);
                for (User user : users) {
                    user.getSrps().remove(srp);
                    userRepository.saveAndFlush(user);
                }
                srp.getUsers().clear();
                srpRepository.save(srp);
                srpRepository.deleteById(srpId);
            }
            else {
                srpRepository.deleteById(srpId);
            }
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

    /*给SRP加监控项*/
    @Transactional
    @Override
    public SRP monitorItemAdd(Long srpId,MonitorItem monitorItem) {
        SRP srp = srpRepository.findBySrpIdOrderBySrpId(srpId);
        MonitorItem monitorItem1 = monitorItemService.monitorItemInsert(monitorItem);
//        MonitorItem monitorItem1 = monitorItemRepository.save(monitorItem);

        monitorItem1.setSrpId(srpId);
        srp.getMonitorItems().add(monitorItem1);
        monitorItem1.setSrp(srp);

        SRP srp1 = srpRepository.saveAndFlush(srp);
        monitorItemRepository.saveAndFlush(monitorItem1);
        return srp1;
    }

    /*给SRP减监控项*/
    @Transactional
    @Override
    public void monitorItemSub(Long monitorItemId) {
        if (monitorItemRepository.findByMonitorId(monitorItemId)!=null)
            monitorItemRepository.deleteById(monitorItemId);
    }

    /*给SRP删除多个监控项*/
    @Override
    public void monitorItemListSub(List<Long> monitorItemIds) {
        for (Long monitorId : monitorItemIds){
            monitorItemSub(monitorId);
        }
    }
}
