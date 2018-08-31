package top.watech.backmonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.watech.backmonitor.entity.MonitorItem;
import top.watech.backmonitor.entity.RespEntity;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.enums.RespCode;
import top.watech.backmonitor.repository.MonitorItemRepository;
import top.watech.backmonitor.repository.SrpRepository;
import top.watech.backmonitor.service.MonitorItemService;
import top.watech.backmonitor.service.SRPService;

import java.util.List;
/**
 * Created by fhm on 2018/7/19.
 * @Description:监控项相关的crud接口
 */
@RestController
public class MonitorItemController {

    @Autowired
    MonitorItemRepository monitorItemRepository;
    @Autowired
    MonitorItemService monitorItemService;

    /**
     * 通过id获取监控项(监控项详细配置查看)
     * @param monitorItemId
     * @return
     */
    @GetMapping("/filter/getMonitorById/{monitorId}")
    public RespEntity getMonitorById(@PathVariable("monitorId") Long monitorItemId) {
        MonitorItem monitorItem = monitorItemService.getMonitorItemListById(monitorItemId);

        if (monitorItem != null) {
            return new RespEntity(RespCode.SUCCESS, monitorItem);
        } else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("根据Id获取monitorItem失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /**
     * 根据srpId查所有监控项，并根据MonitorType排序(显示srp的监控项列表)
     * @param srpId
     * @return
     */
    @GetMapping("/filter/getMonitorsBySrpId")
    public RespEntity getMonitorsBySrpId(@RequestParam("srpId") Long srpId) {
        List<MonitorItem> monitTtemListBySrpId = monitorItemService.getMonitTtemListBySrpIdOrder(srpId);

        if (monitTtemListBySrpId != null) {
            return new RespEntity(RespCode.SUCCESS, monitTtemListBySrpId);
        } else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("根据srpId获取monitorItems失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /**
     * 监控项新增(可能用不着，因为监控项不能在脱离SRP的情况下新增)
     * @param monitorItem
     * @return
     */
    @PostMapping("/filter/monitorItemInsert")
    public RespEntity monitorItemInsert(@RequestBody MonitorItem monitorItem) {
        SRP monitorItem1 = monitorItemService.monitorItemInsert(monitorItem);
        if (monitorItem1 != null) {
            return new RespEntity(RespCode.SUCCESS, monitorItem1);
        } else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("添加监控项失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /**
     * 更新监控项信息
     * @param monitorItem
     * @return
     */
    /*更新监控项信息*/
    @PutMapping("/filter/monitorItemUpdate")
    public RespEntity monitorItemUpdate(@RequestBody MonitorItem monitorItem) {
        MonitorItem monitorItem1 = monitorItemService.monitorItemUpdate(monitorItem);
        if (monitorItem1 != null) {
            return new RespEntity(RespCode.SUCCESS, monitorItem1);
        } else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("更新监控项失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /**
     * 删除一个监控项（相当于给SRP减监控项那个接口）
     * @param monitorItemId
     * @return
     */
    @DeleteMapping("/filter/deletemonitorItemById")
    public RespEntity deletemonitorItemById(@RequestParam("monitorItemId") Long monitorItemId) {
        monitorItemService.deleteById(monitorItemId);
        if (monitorItemRepository.findByMonitorId(monitorItemId) != null)
            return new RespEntity(RespCode.SUCCESS);
        else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("删除监控项失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }

    /**
     * 删除多个监控项
     * @param monitorItemIds
     * @return
     */
    @DeleteMapping("/filter/delemonitorItemList/{monitorItemIds}")
    public RespEntity deletemonitorItemlist(@PathVariable List<Long> monitorItemIds) {
        monitorItemService.deletemonitorItemlist(monitorItemIds);
        return new RespEntity(RespCode.SUCCESS);
    }
}
