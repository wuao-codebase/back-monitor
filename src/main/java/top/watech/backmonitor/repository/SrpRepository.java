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

    /**
     * 根据userId获取srp列表
     * @param userId
     * @return
     */
    @Query(value = "select srps.srp_id,srps.srp_name,srps.description,srps.freq,srps.switchs FROM srps left join user_srp on srps.srp_id = user_srp.srp_id where user_srp.user_id=? order by srp_id", nativeQuery = true)
    List<Object[]> findSrpByUserId(Long userId);

    /**
     * 获取所有SRP，并根据id排序
     * @return
     */
    @Query(value = "select srpId,srpName,description,freq,switchs from SRP order by srpId")
    List<Object[]> findAllSrps();

    /**
     * 根据id获取srp并排序
     * @param srpId
     * @return
     */
    SRP findBySrpIdOrderBySrpId(Long srpId);

    /**
     * 根据id获取srp
     * @param srpId
     * @return
     */
    SRP findBySrpId(Long srpId);

    @Override
    void delete(SRP srp);

    /**
     * 显示用户列表
     * @return
     */
    @Query(value = "select users.user_id,users.role,users.phone,users.user_pwd, string_agg(srps.srp_name,'，') as srpnames,users.user_name from users left join user_srp on users.user_id = user_srp.user_id left join srps on user_srp.srp_id = srps.srp_id group by users.user_id order by user_id", nativeQuery = true)
    List<Object[]> getusers();
}
