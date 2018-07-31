package top.watech.backmonitor.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import top.watech.backmonitor.entity.User;

import java.util.List;

/**
 * Created by fhm on 2018/7/19.
 */
public interface UserRepository extends JpaRepository<User,Long> ,JpaSpecificationExecutor<User>{

        //获得单个用户对象，根据username和pwd的字段匹配
    User getByUserIdIsAndAndUserPwdIs(Long userid, String userpwd);

//    //查询用户名称包含username字符串的用户对象
//    List<User> findByUsernameContaining(String username);
//
//    //获得单个用户对象，根据username和pwd的字段匹配
//    User getByUsernameIsAndUserpwdIs(String username, String userpwd);
//
    //精确匹配username的用户对象
//    User getByUsernameIs(String username);
//
//    @Modifying
//    @Query("update User u set u.username = :username where u.userId= :userId")
//    void updateUserUsername(@Param("userId") Long userid, @Param("username") String username);

    //根据用户名获取用户
    User findByUserName(String userName);

    User findByUserId(Long userId);

    void deleteById(Long aLong);

    @Modifying
    @Query("UPDATE User u SET u.email = :email WHERE userId = :userId")
    void updateUserEmail(@Param("userId") Long id, @Param("email") String email);

    @Query(value = "select users.user_id,users.email,users.role,users.phone,users.user_pwd,users.remark, string_agg(srps.srp_name,'，') as srpnames from users left join user_srp on users.user_id = user_srp.user_id left join srps on user_srp.srp_id = srps.srp_id group by users.user_id", nativeQuery = true)
    List<Object[]> getuserlist();

}
