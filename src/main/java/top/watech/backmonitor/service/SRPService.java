package top.watech.backmonitor.service;

import top.watech.backmonitor.entity.MonitorItem;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.entity.User;

import java.util.List;

/**
 * Created by fhm on 2018/7/23.
 */

public interface SRPService {
    /**
     * 获取SRP列表
     * @return
     */
    List<SRP> getsrpList();

    /**
     * 根据userId获取SRP列表
     * @param userId
     * @return
     */
    List<SRP> findSrpsByUserId(Long userId);

    /**
     * 根据srpId获取SRP
     * @param srpId
     * @return
     */
    SRP getSrpById(Long srpId);

    /**
     * 新增SRP
     * @param srp
     * @param userIds
     * @return
     */
    SRP srpInsert(SRP srp,List<Long> userIds);

    /**
     * 更新SRP
     * @param srp
     * @return
     */
    SRP srpUpdate(SRP srp);

    /**
     * 给SRP加所属用户
     * @param srpId
     * @param userIds
     * @return
     */
    int userAdd(Long srpId, List<Long> userIds);

    /**
     * 给SRP减所属用户
     * @param srpId
     * @param userId
     * @return
     */
    int userSub(Long srpId, Long userId);

    /**
     * 删除一个SRP
     * @param srpId
     */
    void deleteById(Long srpId);

    /**
     * 删除多个SRP
     * @param srpIDs
     */
    void deleteSrplist(List<Long> srpIDs);

    /**
     * 根据userId获取srp
     * @param userId
     * @return
     */
    List<SRP> findByUserId(Long userId);

    /**
     * 显示用户列表
     * @return
     */
    List<User> getUserList();

    /**
     * 给SRP加监控项
     * @param monitorItem
     * @return
     */
    SRP monitorItemAdd(MonitorItem monitorItem);

    /**
     * 给SRP减监控项
     * @param monitorItemId
     */
    void monitorItemSub(Long monitorItemId);

    /**
     * 给SRP删除多个监控项
     * @param monitorItemIds
     */
    void monitorItemListSub(List<Long> monitorItemIds);
}
