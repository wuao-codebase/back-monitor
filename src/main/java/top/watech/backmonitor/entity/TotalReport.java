package top.watech.backmonitor.entity;

import javax.persistence.*;
import java.sql.Time;

/**
 * Created by fhm on 2018/7/24.
 */
@Entity
@Table(name = "total_report")
@IdClass(SRPTimeMultiKey.class)
public class TotalReport {
    private Long srpId;         //复合主键
    private Time createTime;    //复合主键
    private Integer monitorNum; //监控项个数
    private Integer errorCount; //失败数

    @Id
    @Column(name = "srp_id")
    public Long getSrpId() {
        return srpId;
    }

    public void setSrpId(Long srpId) {
        this.srpId = srpId;
    }

    @Id
    @Column(name = "create_time")
    public Time getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Time createTime) {
        this.createTime = createTime;
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
                ", createTime=" + createTime +
                ", monitorNum=" + monitorNum +
                ", errorCount=" + errorCount +
                '}';
    }
}
