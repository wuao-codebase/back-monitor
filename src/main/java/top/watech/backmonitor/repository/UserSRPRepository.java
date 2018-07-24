package top.watech.backmonitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.watech.backmonitor.entity.UserSRP;
import top.watech.backmonitor.entity.UserSRPMultiKey;

import java.util.List;

/**
 * Created by fhm on 2018/7/24.
 */
public interface UserSRPRepository extends JpaRepository<UserSRP,UserSRPMultiKey> {
    // 根据用户Id,找出用户参与的所有SRP
    List<UserSRP> findByUserId(Long userId);

    // 根据SRP的id,找出所属的所有User
    List<UserSRP> findBySrpId(Long srpId);

    // 根据用户id和SRP的id 找出所有的UserSRP
    List<UserSRP> findByUserIdAndSrpId(Long userId,Long srpId);
}
