package top.watech.backmonitor.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by fhm on 2018/7/24.
 */
@Entity
@ToString
@Table(name = "total_report")
public class TotalReport {

    private String uuid;    //主键

    private Long srpId;     //
    private Integer monitorNum; //监控项个数
    private Integer errorCount; //失败数
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private SRP srp;

    private String srpName;

    private List<DetailReport> detailReports;

    @Id @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(length = 100)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    //    @Id
    @Column(name = "srp_id",insertable=false,updatable=false)
    public Long getSrpId() {
        return srpId;
    }

    public void setSrpId(Long srpId) {
        this.srpId = srpId;
    }

//    @Id
    @Column(name = "start_time")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Column(name = "end_time")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Column(name = "monitor_num")
    public Integer getMonitorNum() {
        return monitorNum;
    }

    public void setMonitorNum(Integer monitorNum) {
        this.monitorNum = monitorNum;
    }

    @Column(name = "error_count")
    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    @Transient
    public String getSrpName() {
        return srpName;
    }

    public void setSrpName(String srpName) {
        this.srpName = srpName;
    }

    @JsonBackReference(value = "srp-profiles")
    @JoinColumn(name = "srp_id")
    @ManyToOne//(fetch = FetchType.EAGER)
    public SRP getSrp() {
        return srp;
    }

    public void setSrp(SRP srp) {
        this.srp = srp;
    }

    //
    @JsonBackReference(value = "detailReports-profiles")
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "totalReport",cascade = CascadeType.REMOVE)
    public List<DetailReport> getDetailReports() {
        return detailReports;
    }

    public void setDetailReports(List<DetailReport> detailReports) {
        this.detailReports = detailReports;
    }

    @Override
    public String toString() {
        return "TotalReport{" +
                "uuid='" + uuid + '\'' +
                ", srpId=" + srpId +
                ", monitorNum=" + monitorNum +
                ", errorCount=" + errorCount +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
