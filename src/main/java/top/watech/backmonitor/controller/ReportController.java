package top.watech.backmonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.watech.backmonitor.entity.PageEntity;
import top.watech.backmonitor.entity.RespEntity;
import top.watech.backmonitor.entity.TotalReport;
import top.watech.backmonitor.enums.RespCode;
import top.watech.backmonitor.service.TotalReportService;

/**
 * Created by wuao.tp on 2018/8/9.
 */
@RestController
public class ReportController {
    @Autowired
    private TotalReportService totalReportService;

    @PostMapping("/toatalList/{pageNo}/{role}/{userId}")
    public RespEntity getsrpList(@RequestBody TotalReport totalReport, @PathVariable int pageNo , @PathVariable int role,Long userId) {
        System.err.println(totalReport);
        System.err.println(pageNo);
        System.err.println(role);  System.err.println(userId);

      PageEntity toList = totalReportService.getTOList( pageNo, role, totalReport);

        if(toList==null )
        {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("查询为空");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }else {
            return new RespEntity(RespCode.SUCCESS,toList);
        }
    }

}
