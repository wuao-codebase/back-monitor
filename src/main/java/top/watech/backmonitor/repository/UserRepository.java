package top.watech.backmonitor.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.watech.backmonitor.entity.User;

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


}
