package top.watech.backmonitor.repository;


import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.watech.backmonitor.entity.MonitorItem;

import java.util.List;

/**
 * Created by wuao.tp on 2018/7/18.
 */
public interface MonitorItemRepository extends JpaRepository<MonitorItem,Long> {

    //根据SRPid查
//    @Override
//    List<MonitorItem> findAll();

    //根据srpId查所有监控项，并根据classify排序
    List<MonitorItem> findBySrpIdOrderByClassify(Long srpId);

    MonitorItem findByMonitorId(Long mobitorItemId);

}
