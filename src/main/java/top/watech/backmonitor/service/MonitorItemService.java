package top.watech.backmonitor.service;

import top.watech.backmonitor.entity.MonitorItem;

import java.util.List;

/**
 * Created by fhm on 2018/7/26.
 */
public interface MonitorItemService {

    /*根据srpId查所有监控项，并根据classify排序(监控逻辑用)*/
    List<MonitorItem> getMonitTtemListBySrpId(Long srp_id);

    /*根据srpId查所有监控项，并根据MonitorType排序(显示srp的监控项列表)*/
    List<MonitorItem> getMonitTtemListBySrpIdOrder(Long srp_id);

    /*通过id获取监控项*/
    MonitorItem getMonitorItemListById(Long monitorItemId);

    /*新增监控项*/
    MonitorItem monitorItemInsert(MonitorItem monitorItem);

    /*更新监控项*/
    MonitorItem monitorItemUpdate(MonitorItem monitorItem);

    /*删除一个监控项*/
    void deleteById(Long monitorItemId);

    /*删除多个监控项*/
    void deletemonitorItemlist(List<Long> monitorItemIds);
}
