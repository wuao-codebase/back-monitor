package top.watech.backmonitor.service;

import top.watech.backmonitor.entity.PageEntity;
import top.watech.backmonitor.entity.TotalReport;

import java.sql.Time;
import java.util.List;

/**
 * Created by fhm on 2018/7/23.
 */

public interface TotalReportService {
    List<TotalReport> getTotalReportList(Time createTime);

    /**
     * 监控报告列表
     * @param pageNo
     * @param role
     * @param totalReport
     * @param userId
     * @return
     */
    PageEntity getTOList(int pageNo,int role,TotalReport totalReport,Long userId);
}
