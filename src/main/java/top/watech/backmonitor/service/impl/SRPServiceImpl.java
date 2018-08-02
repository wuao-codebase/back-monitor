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

    @Override
    public List<SRP> getsrpList() {
        return null;
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

    /*删除一个srp*/
    @Transactional
    @Override
    public void deleteById(Long srpId) {
//        SRP srp = srpRepository.findBySrpId(srpId);
//        if (srp!=null) {
//            List<User> users = userService.getUserBySrpId(srpId);
//            for (User user : users){
//                user.getSrps().remove(srp);
//            }
////            srp.getMonitorItems().clear();
//            srpRepository.save(srp);
//            userRepository.saveAll(users);
//            srpRepository.deleteById(srpId);
//        }

    }

    /*删除多个srp*/
    @Transactional
    @Override
    public void deleteSrplist(List<Long> srpIDs) {
        for (Long srpId : srpIDs){
            deleteById(srpId);
        }
    }

    //根据userId获取SRPname
    @Override
    public List<SRP> findByUserId(String userId) {
        List<SRP> srpList = srpRepository.findAll(new Specification<SRP>() {
            public Predicate toPredicate(Root<SRP> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join<SRP, User> userJoin = root.join("users", JoinType.LEFT);
                return cb.equal(userJoin.get("userId"), userId);
            }
        });

        return srpList;
    }
}
