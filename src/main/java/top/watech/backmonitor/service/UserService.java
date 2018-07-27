package top.watech.backmonitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.watech.backmonitor.entity.User;
import top.watech.backmonitor.repository.UserRepository;

import java.util.List;

/**
 * Created by fhm on 2018/7/23.
 */

public interface UserService {
    List<User> getUserList();
}
