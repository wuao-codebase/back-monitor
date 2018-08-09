package top.watech.backmonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.watech.backmonitor.entity.PageEntity;
import top.watech.backmonitor.entity.RespEntity;
import top.watech.backmonitor.entity.User;
import top.watech.backmonitor.enums.RespCode;
import top.watech.backmonitor.service.TotalReportService;

/**
 * Created by wuao.tp on 2018/8/9.
 */
@RestController
public class ReportController {
    @Autowired
    private TotalReportService totalReportService;

    @GetMapping("/toatalList/{pageNo}")
    public RespEntity getsrpList(@RequestBody User user, @PathVariable int pageNo ) {


        PageEntity toList = totalReportService.getTOList(pageNo);

        return new RespEntity(RespCode.SUCCESS,toList);
    }

}
