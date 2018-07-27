package top.watech.backmonitor.service;

import top.watech.backmonitor.entity.MonitorItem;
import top.watech.backmonitor.entity.SRP;

import java.util.List;

/**
 * Created by fhm on 2018/7/26.
 */
public interface MonitorItemService {
    List<MonitorItem> getMonitorItemList();

    List<MonitorItem> getMonitTtemList(Long srp_id);
}
