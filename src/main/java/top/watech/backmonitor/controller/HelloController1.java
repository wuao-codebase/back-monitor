package top.watech.backmonitor.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wuao.tp on 2018/7/9.
 */
@RestController
public class HelloController1 {

    @GetMapping("/")
    public String getStudent(){

        return "123123";
    }


}
