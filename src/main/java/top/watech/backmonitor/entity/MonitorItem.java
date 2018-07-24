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
    private Integer monitorType;    //监控项类型
    private Integer requestType;    //请求类型
    private String url;

    private String requestBody;     //请求体
    private String asserts;         //断言

    private Long srpId;             //外键

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

    //加不加@ManyToOne(fetch = FetchType.EAGER)？
    @JoinColumn(name = "srp_id")
    public Long getSrpId() {
        return srpId;
    }

    public void setSrpId(Long srpId) {
        this.srpId = srpId;
    }
}
