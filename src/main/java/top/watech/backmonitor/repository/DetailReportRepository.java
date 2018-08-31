package top.watech.backmonitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.watech.backmonitor.entity.DetailReport;
import top.watech.backmonitor.entity.MonitorItemTimeMultiKey;

import java.sql.Time;
import java.util.List;

/**
 * Created by fhm on 2018/7/24.
 */
public interface DetailReportRepository extends JpaRepository<DetailReport,MonitorItemTimeMultiKey>{

    /**
     * uuid找出所有详细监控报告
     * @param uuid
     * @return
     */
    List<DetailReport> findByUuid(String uuid);

    /**
     * monitorId,找出所有详细监控报告
     * @param monitorId
     * @return
     */
    List<DetailReport> findByMonitorId(Long monitorId);

    /**
     * 根据监控执行时间,找出所有详细监控报告
     * @param createTime
     * @return
     */
    List<DetailReport> findByCreateTime(Time createTime);

    /**
     * 根据monitorId和监控执行开始时间，找出所有的DetailReport
     * @param monitorId
     * @param createTime
     * @return
     */
    List<DetailReport> findByMonitorIdAndCreateTime(Long monitorId, Time createTime);

    /**
     * 根据uuId，找出所有的DetailReport,根据监控执行开始时间排序
     * @param uuid
     * @return
     */
    List<DetailReport> findByUuidOrderByCreateTime(String uuid);
}
