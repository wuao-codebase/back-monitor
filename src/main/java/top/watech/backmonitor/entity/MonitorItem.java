package top.watech.backmonitor.entity;

import javax.persistence.*;

/**
 * Created by fhm on 2018/7/24.
 */
@Entity
@Table(name = "monitoritems")
public class MonitorItem {
    private Long monitorId;
    private String monitorName;
    private Integer monitorType;    //监控项类型:1、接口 2、视频 3、页面
    private Integer requestType;    //请求类型
    private String url;

    private String requestBody;     //请求体
    private String asserts;         //断言(预期返回结果)
    private String remark;  //备注

    private Integer classify;//分类：1、平台登陆 2、平台接口 3、SRP登陆 4、SRP接口

    private Long srpId;

    private SRP srp;             //外键

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "monitor_id")
    public Long getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(Long monitorId) {
        this.monitorId = monitorId;
    }

    @Column(name = "monitor_name")
    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    @Column(name = "monitor_type")
    public Integer getMonitorType() {
        return monitorType;
    }

    public void setMonitorType(Integer monitorType) {
        this.monitorType = monitorType;
    }

    @Column(name = "request_type")
    public Integer getRequestType() {
        return requestType;
    }

    public void setRequestType(Integer requestType) {
        this.requestType = requestType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "request_body")
    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getAsserts() {
        return asserts;
    }

    public void setAsserts(String asserts) {
        this.asserts = asserts;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getClassify() {
        return classify;
    }

    public void setClassify(Integer classify) {
        this.classify = classify;
    }


//    public Long getSrp_id() {
//        return srp_id;
//    }
//
//    public void setSrp_id(Long srp_id) {
//        this.srp_id = srp_id;
//    }

    @Column(name="srp_id",insertable=false,updatable=false)
    public Long getSrpId() {
        return srpId;
    } public void setSrpId(Long srpId) {
        this.srpId = srpId;
    }
    @JoinColumn(name = "srp_id")
    @ManyToOne(fetch = FetchType.LAZY)
    public SRP getSrp() {
        return srp;
    }

    public void setSrp(SRP srp) {
        this.srp = srp;
    }

}
