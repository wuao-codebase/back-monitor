package top.watech.backmonitor.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.watech.backmonitor.entity.SRP;

/**
 * Created by fhm on 2018/7/19.
 */
public interface SrpRepository extends JpaRepository<SRP,Long> ,JpaSpecificationExecutor<SRP>{


}
