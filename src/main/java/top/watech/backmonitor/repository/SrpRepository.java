package top.watech.backmonitor.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import top.watech.backmonitor.entity.SRP;

import java.util.List;

/**
 * Created by fhm on 2018/7/19.
 */
public interface SrpRepository extends JpaRepository<SRP,Long> ,JpaSpecificationExecutor<SRP>{

    //根据srpId和userName获取srp
//    SRP findSRPBySrpIdAndUserName();

    //根据userId获取srp列表
//    List<SRP> findSRPSByuserId(Long userId);

    SRP findBySrpId(Long srpId);

    @Modifying
    @Query(value = "delete from SRP s where s.srpId = :srpId ")
    int deleteBySrpId(Long srpId);


    void deleteById(Long aLong);

//    @Modifying
//    @Query("UPDATE User u SET u.email = :email WHERE userId = :userId")
//    void updateUserEmail(@Param("userId") Long id, @Param("email") String email);

}
