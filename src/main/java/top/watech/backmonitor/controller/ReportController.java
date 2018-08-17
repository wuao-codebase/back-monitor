package top.watech.backmonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.watech.backmonitor.entity.DetailReport;
import top.watech.backmonitor.entity.PageEntity;
import top.watech.backmonitor.entity.RespEntity;
import top.watech.backmonitor.entity.TotalReport;
import top.watech.backmonitor.enums.RespCode;
import top.watech.backmonitor.service.DetailReportService;
import top.watech.backmonitor.service.TotalReportService;

import java.util.List;

/**
 * Created by wuao.tp on 2018/8/9.
 */
@RestController
public class ReportController {
    @Autowired
    private TotalReportService totalReportService;
    @Autowired
    private DetailReportService detailReportService;

    /*取监控列表*/
    @PostMapping("/toatalList/{pageNo}/{role}/{userId}")
    public RespEntity gettotalList(@RequestBody TotalReport totalReport, @PathVariable int pageNo , @PathVariable int role, @PathVariable Long userId) {
        System.err.println(totalReport);
        System.err.println(pageNo);
        System.err.println(role);  System.err.println(userId);

      PageEntity toList = totalReportService.getTOList( pageNo, role, totalReport,userId);

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

    /*取详细监控报告列表*/
    @GetMapping("/detailList/{uuid}")
    public RespEntity getdetailList(@PathVariable String uuid) {
        System.err.println("uuid = " + uuid);
        List<DetailReport> detailReportByUuid = detailReportService.getDetailReportByUuid(uuid);

        if(detailReportByUuid==null )
        {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("查询为空");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }else {
            return new RespEntity(RespCode.SUCCESS,detailReportByUuid);
        }
    }

}
