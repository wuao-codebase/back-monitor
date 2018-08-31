package top.watech.backmonitor.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import top.watech.backmonitor.entity.User;

import java.util.List;

/**
 * Created by fhm on 2018/7/19.
 */
public interface UserRepository extends JpaRepository<User,Long> ,JpaSpecificationExecutor<User>{

    /**
     * 获得单个用户对象，根据username和pwd的字段匹配
     * @param userid
     * @param userpwd
     * @return
     */
    User getByUserIdIsAndAndUserPwdIs(Long userid, String userpwd);

    /**
     * 根据用户名获取用户
     * @param userName
     * @return
     */
    User findByUserName(String userName);

    /**
     * 根据手机号获取用户
     * @param phone
     * @return
     */
    User findByPhone(Long phone);

    /**
     * 根据userId获取用户
     * @param userId
     * @return
     */
    User findByUserId(Long userId);

    /**
     * 获取用户列表，包括用户基本信息和srpName
     * @return
     */
    @Query(value = "select users.user_id,users.email,users.role,users.phone,users.user_pwd,users.remark, string_agg(srps.srp_name,'，') as srpnames,users.user_name from users left join user_srp on users.user_id = user_srp.user_id left join srps on user_srp.srp_id = srps.srp_id group by users.user_id order by user_id", nativeQuery = true)
    List<Object[]> getuserlist();

    /**
     * id不是？且手机号是？的用户
     * @param userId
     * @param phone
     * @return
     */
    User findByUserIdIsNotAndPhoneIs(Long userId,Long phone);
}
