package top.watech.backmonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.watech.backmonitor.entity.*;
import top.watech.backmonitor.enums.RespCode;
import top.watech.backmonitor.repository.UserRepository;
import top.watech.backmonitor.service.UserService;
import top.watech.backmonitor.util.JwtHelper;
import top.watech.backmonitor.util.ResultVOUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by fhm on 2018/7/19.
 */
@RestController
public class SrpController {

    @Autowired
    private UserService userService ;
    @Autowired
    UserRepository userRepository;

    @Autowired
    private Audience audience;


}
