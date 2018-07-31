package top.watech.backmonitor.service;

import top.watech.backmonitor.entity.SRP;

import java.util.List;

/**
 * Created by fhm on 2018/7/23.
 */

public interface SRPService {

    List<SRP> findByUserId(String userId);

    int deleteBySrpId(Long srpId);


}
