package top.watech.backmonitor.entity;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.sql.Date;
import java.sql.Time;

/**
 * Created by fhm on 2018/7/24.
 */
@Entity
@Table(name = "total_report")
@IdClass(SRPTimeMultiKey.class)
public class TotalReport {
    private Long srpId;         //复合主键
    private Integer monitorNum; //监控项个数
    private Integer errorCount; //失败数

    private Date startTime;     //复合主键,监控开始执行时间
    private Date endTime;       //监控结束执行时间

    @Id
    @Column(name = "srp_id")
    public Long getSrpId() {
        return srpId;
    }

    public void setSrpId(Long srpId) {
        this.srpId = srpId;
    }

    @Id
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

    @Override
    public String toString() {
        return "TotalReport{" +
                "srpId=" + srpId +
                ", monitorNum=" + monitorNum +
                ", errorCount=" + errorCount +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
