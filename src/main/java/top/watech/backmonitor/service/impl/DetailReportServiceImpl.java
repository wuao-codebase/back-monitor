package top.watech.backmonitor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.watech.backmonitor.entity.DetailReport;
import top.watech.backmonitor.repository.DetailReportRepository;
import top.watech.backmonitor.service.DetailReportService;

import java.sql.Time;
import java.util.List;

/**
 * DetailReportService接口实现类
 * Created by fhm on 2018/7/27.
 */

@Service
public class DetailReportServiceImpl implements DetailReportService {
    @Autowired
    DetailReportRepository detailReportRepository;

    //根据时间取所有SRP的监控项结果
    @Override
    public List<DetailReport> getDetailReportList(Time createTime) {
        return detailReportRepository.findByCreateTime(createTime);
    }

    @Override
    public List<DetailReport> getDetailReportByUuid(String uuid) {
        List<DetailReport> byUuid = detailReportRepository.findByUuidOrderByCreateTime(uuid);
        return byUuid;
    }
}
