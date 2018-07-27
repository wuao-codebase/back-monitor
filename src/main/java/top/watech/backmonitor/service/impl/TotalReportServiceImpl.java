package top.watech.backmonitor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.entity.TotalReport;
import top.watech.backmonitor.repository.SrpRepository;
import top.watech.backmonitor.repository.TotalReportRepository;
import top.watech.backmonitor.service.SRPService;
import top.watech.backmonitor.service.TotalReportService;

import java.sql.Time;
import java.util.List;

/**
 * Created by fhm on 2018/7/27.
 */

@Service
public class TotalReportServiceImpl implements TotalReportService {
    @Autowired
    TotalReportRepository totalReportRepository;

    //根据时间取所有SRP的监控结果
    @Override
    public List<TotalReport> getTotalReportList(Time createTime) {
        return totalReportRepository.findByStartTime(createTime);
    }

//    @Transactional
//    public void updateUsers(Long userId,String username){
//        userRepository.updateUserUsername(userId,username);
//    }
}
