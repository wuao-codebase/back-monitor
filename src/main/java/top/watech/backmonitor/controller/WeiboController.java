package top.watech.backmonitor.controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.watech.backmonitor.entity.Weibo;
import top.watech.backmonitor.repository.WeiboRepository;

/**
 * Created by fhm on 2018/7/19.
 */
@RestController
public class WeiboController {

    WeiboRepository weiboRepository;
    public Weibo getUserWeibo(@PathVariable("username") String username){
        return this.weiboRepository.searchUserWeibo(username).get(1);
    }

}
