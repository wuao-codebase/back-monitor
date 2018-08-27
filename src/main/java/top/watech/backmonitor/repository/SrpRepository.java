package top.watech.backmonitor.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.entity.User;

import java.util.List;

/**
 * Created by fhm on 2018/7/19.
 */
public interface SrpRepository extends JpaRepository<SRP,Long> ,JpaSpecificationExecutor<SRP>{

    //根据userId获取srp列表
    @Query(value = "select srps.srp_id,srps.srp_name,srps.description,srps.freq,srps.switchs FROM srps left join user_srp on srps.srp_id = user_srp.srp_id where user_srp.user_id=? order by srp_id", nativeQuery = true)
    List<Object[]> findSrpByUserId(Long userId);

    //获取所有SRP，并根据id排序
    @Query(value = "select srpId,srpName,description,freq,switchs from SRP order by srpId")
    List<Object[]> findAllSrps();
//    List<SRP> findAllByOrderBySrpId();

    /*根据id获取srp*/
    SRP findBySrpIdOrderBySrpId(Long srpId);

    SRP findBySrpId(Long srpId);

    @Modifying
    @Query(value = "delete from SRP s where s.srpId = :srpId ")
    int deleteBySrpId(Long srpId);

    @Override
    void delete(SRP srp);

    //显示用户列表
    @Query(value = "select users.user_id,users.role,users.phone,users.user_pwd, string_agg(srps.srp_name,'，') as srpnames,users.user_name from users left join user_srp on users.user_id = user_srp.user_id left join srps on user_srp.srp_id = srps.srp_id group by users.user_id order by user_id", nativeQuery = true)
    List<Object[]> getusers();

//    @Modifying
//    @Query("UPDATE User u SET u.email = :email WHERE userId = :userId")
//    void updateUserEmail(@Param("userId") Long id, @Param("email") String email);

    //根据srpId和userName获取srp
//    SRP findSRPBySrpIdAndUserName();
}
