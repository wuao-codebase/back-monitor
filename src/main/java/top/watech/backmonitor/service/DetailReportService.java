package top.watech.backmonitor.service;

import top.watech.backmonitor.entity.DetailReport;

import java.sql.Time;
import java.util.List;

/**
 * Created by fhm on 2018/7/23.
 */

public interface DetailReportService {
    List<DetailReport> getDetailReportList(Time createTime);

    List<DetailReport> getDetailReportByUuid(String uuid);
}
