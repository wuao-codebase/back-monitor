package top.watech.backmonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.watech.backmonitor.entity.RespEntity;
import top.watech.backmonitor.entity.VCMInfo;
import top.watech.backmonitor.enums.RespCode;
import top.watech.backmonitor.service.VCMInfomation;

import java.util.List;

/**
 * Created by fhm on 2018/8/17.
 * @Description:视频监控项配置的时候，下拉列表显示要调的接口
 */
@RestController
public class VCMInfoController {
    @Autowired
    VCMInfomation vcmInfomation;

    /**
     * 取VCM信息列表
     * @return
     * @throws Exception
     */
    @GetMapping("/filter/VCMInfoList")
    public RespEntity VCMInfoList() throws Exception {
        List<VCMInfo> vcmInfoList = vcmInfomation.getVCMInfos();
        if (vcmInfoList != null) {
            return new RespEntity(RespCode.SUCCESS, vcmInfoList);
        } else {
            RespCode respCode = RespCode.WARN;
            respCode.setMsg("获取VCM信息失败");
            respCode.setCode(-1);
            return new RespEntity(respCode);
        }
    }
}
