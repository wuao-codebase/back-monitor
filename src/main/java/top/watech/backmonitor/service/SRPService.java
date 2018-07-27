package top.watech.backmonitor.service;

import org.springframework.stereotype.Service;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.entity.User;

import java.util.List;

/**
 * Created by fhm on 2018/7/23.
 */

public interface SRPService {
    List<SRP> getSRPList();
}
