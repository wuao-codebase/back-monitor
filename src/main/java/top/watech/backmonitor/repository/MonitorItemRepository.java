package top.watech.backmonitor.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import top.watech.backmonitor.entity.MonitorItem;

import java.util.List;

/**
 * Created by wuao.tp on 2018/7/18.
 */
public interface MonitorItemRepository extends JpaRepository<MonitorItem,Long> {

    //根据SRPid查
//    @Override
//    List<MonitorItem> findAll();

    /*根据srpId查所有监控项，并根据classify排序(srp的监控逻辑)*/
    List<MonitorItem> findBySrpIdOrderByClassify(Long srpId);

    /*根据srpId查所有监控项，并根据MonitorType排序(显示srp的监控项列表)*/
    List<MonitorItem> findBySrpIdOrderByMonitorType(Long srpId);

    /*通过id获取监控项*/
    MonitorItem findByMonitorId(Long mobitorItemId);

    /*通过name获取监控项*/
    MonitorItem findByMonitorName(String mobitorItemName);


//    @Query(value = "select * from xxx where if(?1 !='',x1=?1,1=1) and if(?2 !='',x2=?2,1=1)" +
//            "and if(?3 !='',x3=?3,1=1)  ",nativeQuery = true)
//    List<MonitorItem> find(String X1,String X2,String X3);
}
