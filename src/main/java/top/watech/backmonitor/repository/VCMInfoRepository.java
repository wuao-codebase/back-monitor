package top.watech.backmonitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.watech.backmonitor.entity.DetailReport;
import top.watech.backmonitor.entity.MonitorItemTimeMultiKey;
import top.watech.backmonitor.entity.VCMInfo;

import java.sql.Time;
import java.util.List;

/**
 * Created by fhm on 2018/7/24.
 */
public interface VCMInfoRepository extends JpaRepository<VCMInfo,Integer>{

}
