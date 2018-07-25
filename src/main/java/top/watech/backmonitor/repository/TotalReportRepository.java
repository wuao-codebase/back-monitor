package top.watech.backmonitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.watech.backmonitor.entity.SRPTimeMultiKey;
import top.watech.backmonitor.entity.TotalReport;
import top.watech.backmonitor.entity.UserSRP;

import java.sql.Time;
import java.util.List;

/**
 * Created by fhm on 2018/7/24.
 */
public interface TotalReportRepository extends JpaRepository<TotalReport,SRPTimeMultiKey>{
    // 根据srpId,找出所有监控报告
    List<TotalReport> findBySrpId(Long srpId);

    // 根据监控执行时间,找出所有监控报告
    List<TotalReport> findByStartTime(Time startTime);

    // 根据SRP的id和监控执行开始时间，找出所有的TotalReport
    List<TotalReport> findBySrpIdAndStartTime(Long srpId,Time startTime);
}
