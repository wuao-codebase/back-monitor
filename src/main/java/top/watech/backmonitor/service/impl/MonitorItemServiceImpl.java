package top.watech.backmonitor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.watech.backmonitor.entity.MonitorItem;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.repository.MonitorItemRepository;
import top.watech.backmonitor.service.MonitorItemService;

import java.util.List;

/**
 * Created by fhm on 2018/7/27.
 */

@Service
public class MonitorItemServiceImpl implements MonitorItemService {
    @Autowired
    MonitorItemRepository monitorItemRepository;


    @Override
    public List<MonitorItem> getMonitorItemList() {
        List<MonitorItem> list = monitorItemRepository.findAll();
        return list;
    }

    @Override
    public List<MonitorItem> getMonitTtemList(Long srpId) {
        List<MonitorItem> monitorItemList = monitorItemRepository.findBySrpIdOrderByClassify(srpId);
        return monitorItemList;
    }
}
