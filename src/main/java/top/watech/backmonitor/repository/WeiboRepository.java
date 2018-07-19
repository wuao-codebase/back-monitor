package top.watech.backmonitor.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import top.watech.backmonitor.entity.Weibo;
import top.watech.backmonitor.entity.User;

import java.util.List;

/**
 * Created by fhm on 2018/7/19.
 */
public interface WeiboRepository extends JpaRepository<Weibo,Long>,JpaSpecificationExecutor<Weibo> {
    //查某用户微博
    @Query("select w from Weibo w where w.user.username = :username")
       List<Weibo> searchUserWeibo(@Param("username") String username);

    //排序
    @Query("select w from Weibo w where w.user.username = :username")
    List<Weibo> searchUserWeibo(@Param("username") String username, Sort sort);

    //改某条微博内容
    @Modifying
    @Transactional(readOnly = false)
    @Query("update Weibo w set w.weiboText = :text where w.user = :user")
    int setUserWeiboContent(@Param("text") String weiboText, @Param("user") User user);

    //返回值是一个泛型对象Page<T>
    Page<Weibo> findByUserIsAndWeiboTextContaining(User user, String weiboText, Pageable pageable);
}
