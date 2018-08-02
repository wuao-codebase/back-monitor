package top.watech.backmonitor.service;

import top.watech.backmonitor.entity.SRP;

import java.util.List;

/**
 * Created by fhm on 2018/7/23.
 */

public interface SRPService {

    //获取SRP列表
    List<SRP> getsrpList();

    /*根据srpId获取SRP*/
    SRP getSrpById(Long srpId);

    //新增SRP
    SRP srpInsert(SRP srp);

    //更新SRP
    SRP srpUpdate(SRP srp);

    /*删除一个SRP*/
    void deleteById(Long srpId);

    /*删除多个SRP*/
    void deleteSrplist(List<Long> srpIDs);

    /*根据userId获取srp*/
    List<SRP> findByUserId(Long userId);
}
