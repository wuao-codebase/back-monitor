package top.watech.backmonitor.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import top.watech.backmonitor.entity.MonitorItem;

/**
 * Created by wuao.tp on 2018/7/18.
 */
public interface MonitorItemRepository extends JpaRepository<MonitorItem,Long> {

}
