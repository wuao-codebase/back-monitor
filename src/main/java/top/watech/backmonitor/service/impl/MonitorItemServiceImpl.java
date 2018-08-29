package top.watech.backmonitor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.watech.backmonitor.entity.MonitorItem;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.repository.MonitorItemRepository;
import top.watech.backmonitor.repository.SrpRepository;
import top.watech.backmonitor.service.MonitorItemService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fhm on 2018/7/27.
 */

@Service
public class MonitorItemServiceImpl implements MonitorItemService {
    @Autowired
    MonitorItemRepository monitorItemRepository;
    @Autowired
    SrpRepository srpRepository;

    /*根据srpId查所有监控项，并根据classify排序(监控逻辑用)*/
    @Override
    public List<MonitorItem> getMonitTtemListBySrpId(Long srpId) {
        List<MonitorItem> monitorItemList = monitorItemRepository.findBySrpIdOrderByClassify(srpId);
        List<MonitorItem> monitorItemList2 = new ArrayList<>();
        for (MonitorItem monitorItem : monitorItemList){
            if (!monitorItem.getOld()){
                monitorItemList2.add(monitorItem);
            }
        }
        return monitorItemList2;
    }

    /*根据srpId查所有监控项，并根据MonitorType排序(显示srp的监控项列表)*/
    @Override
    public List<MonitorItem> getMonitTtemListBySrpIdOrder(Long srpId) {
        List<MonitorItem> monitorItemList = monitorItemRepository.findBySrpIdOrderByMonitorType(srpId);
        List<MonitorItem> monitorItemList2 = new ArrayList<>();
        for (MonitorItem monitorItem : monitorItemList){
            if (!monitorItem.getOld()){
                monitorItemList2.add(monitorItem);
            }
        }
        return monitorItemList2;
    }

    /*通过id获取监控项*/
    @Override
    public MonitorItem getMonitorItemListById(Long monitorItemId) {
        MonitorItem monitorItem = monitorItemRepository.findByMonitorId(monitorItemId);
        return monitorItem;
    }

    /*新增监控项*/
    @Transactional
    @Override
    public SRP monitorItemInsert(MonitorItem monitorItem) {
        SRP srp = srpRepository.findBySrpId(monitorItem.getSrpId());
        monitorItem.setOld(false);
        MonitorItem monitorItem1 = monitorItemRepository.save(monitorItem);

        srp.getMonitorItems().add(monitorItem1);
        monitorItem1.setSrp(srp);

        SRP srp1 = srpRepository.saveAndFlush(srp);
        monitorItemRepository.saveAndFlush(monitorItem1);
        return srp1;
    }

    /*更新监控项*/
    @Transactional
    @Override
    public MonitorItem monitorItemUpdate(MonitorItem monitorItem) {
        MonitorItem monitorItem2 = monitorItemRepository.findByMonitorId(monitorItem.getMonitorId());
        monitorItem2.setOld(true);
        monitorItemRepository.saveAndFlush(monitorItem2);
        MonitorItem monitorItem1 = new MonitorItem();
        SRP srp = srpRepository.findBySrpId(monitorItem.getSrpId());
        if (monitorItem!=null){
            monitorItem1.setMonitorName(monitorItem.getMonitorName());
            monitorItem1.setRemark(monitorItem.getRemark());
            monitorItem1.setUrl(monitorItem.getUrl());
            monitorItem1.setRequestType(monitorItem.getRequestType());
            monitorItem1.setRequestBody(monitorItem.getRequestBody());
            monitorItem1.setAsserts(monitorItem.getAsserts());
            monitorItem1.setOld(false);
            monitorItem1.setConnTimeout(monitorItem.getConnTimeout());
            monitorItem1.setReadTimeout(monitorItem.getReadTimeout());
            monitorItem1.setClassify(monitorItem.getClassify());
            monitorItem1.setSrpId(monitorItem.getSrpId());
            monitorItem1.setMonitorType(monitorItem.getMonitorType());

            monitorItem1.setSrp(srp);
            srp.getMonitorItems().add(monitorItem1);

            srpRepository.saveAndFlush(srp);
            return monitorItemRepository.saveAndFlush(monitorItem1);
        }
        return null;
    }

    /*删除一个监控项*/
    @Transactional
    @Override
    public void deleteById(Long monitorItemId) {
        if (monitorItemRepository.findByMonitorId(monitorItemId)!=null)
            monitorItemRepository.deleteById(monitorItemId);
    }

    /*删除多个监控项*/
    @Transactional
    @Override
    public void deletemonitorItemlist(List<Long> monitorItemIds) {
        for (Long monitorItemId : monitorItemIds){
            monitorItemRepository.deleteById(monitorItemId);
        }
    }
}
