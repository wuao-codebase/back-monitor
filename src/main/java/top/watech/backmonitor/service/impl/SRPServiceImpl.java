package top.watech.backmonitor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.entity.User;
import top.watech.backmonitor.repository.SrpRepository;
import top.watech.backmonitor.service.SRPService;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * Created by fhm on 2018/7/27.
 */

@Service
public class SRPServiceImpl implements SRPService {
    @Autowired
    SrpRepository srpRepository;

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

    @Override
    public int deleteBySrpId(Long srpId) {

        return 0;
    }

    /*SRP新增*/
    public SRP srpInsert(SRP srp){
        SRP save = srpRepository.save(srp);
        return save;
    }

    /*srp更新*/
    public SRP srpUpdate(SRP srp){
        return null;
    }
}
