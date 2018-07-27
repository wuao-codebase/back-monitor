package top.watech.backmonitor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.entity.User;
import top.watech.backmonitor.repository.SrpRepository;
import top.watech.backmonitor.repository.UserRepository;
import top.watech.backmonitor.service.SRPService;
import top.watech.backmonitor.service.UserService;

import java.util.List;

/**
 * Created by fhm on 2018/7/27.
 */

@Service
public class SRPServiceImpl implements SRPService {
    @Autowired
    SrpRepository srpRepository;

    @Override
    public List<SRP> getSRPList() {
        return null;
    }

//    @Transactional
//    public void updateUsers(Long userId,String username){
//        userRepository.updateUserUsername(userId,username);
//    }
}
