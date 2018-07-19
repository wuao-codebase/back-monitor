package top.watech.backmonitor.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.watech.backmonitor.repository.StudRepository;

/**
 * Created by wuao.tp on 2018/7/9.
 */
@RestController
public class HelloController1 {
    @Autowired
    StudRepository studRepository;

    @GetMapping("/")
    public String getStudent(){

        return "123123";
    }


}
