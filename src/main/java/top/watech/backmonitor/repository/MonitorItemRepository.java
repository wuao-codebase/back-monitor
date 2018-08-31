package top.watech.backmonitor.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import top.watech.backmonitor.entity.MonitorItem;

import java.util.List;

/**
 * Created by wuao.tp on 2018/7/18.
 */
public interface MonitorItemRepository extends JpaRepository<MonitorItem,Long> {

    /**
     *根据srpId查所有监控项，并根据classify排序(srp的监控逻辑)
     * @param srpId
     * @return
     */
    List<MonitorItem> findBySrpIdOrderByClassify(Long srpId);

    /**
     * 根据srpId查所有监控项，并根据MonitorType排序(显示srp的监控项列表)
     * @param srpId
     * @return
     */
    List<MonitorItem> findBySrpIdOrderByMonitorType(Long srpId);

    /**
     * 通过id获取监控项
     * @param mobitorItemId
     * @return
     */
    MonitorItem findByMonitorId(Long mobitorItemId);

    /**
     * 通过name获取监控项
     * @param mobitorItemName
     * @return
     */
    MonitorItem findByMonitorName(String mobitorItemName);
}
