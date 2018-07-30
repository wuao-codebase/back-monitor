package top.watech.backmonitor.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.watech.backmonitor.entity.SRP;

import java.util.List;

/**
 * Created by fhm on 2018/7/19.
 */
public interface SrpRepository extends JpaRepository<SRP,Long> ,JpaSpecificationExecutor<SRP>{

    //根据srpId和userName获取srp
    SRP findSRPBySrpIdAndUserName();

    //根据userId获取srp列表
    List<SRP> findSRPSByuserId(Long userId);

}
