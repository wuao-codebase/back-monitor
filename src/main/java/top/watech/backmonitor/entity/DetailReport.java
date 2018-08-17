package top.watech.backmonitor.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by fhm on 2018/7/24.
 */


@EntityListeners(AuditingEntityListener.class)
@Entity
@ToString
@Table(name = "detail_report")
@IdClass(MonitorItemTimeMultiKey.class)
public class DetailReport {
    private Long monitorId; //复合主键
    private String monitorName;

    private Boolean code;   //成功、失败
    private String message; //报警信息

    private String messageBody; //返回信息
    private String pre;


    private Date createTime;//复合主键,数据插入时间

    private String uuid;    //外键
    private TotalReport totalReport;


    @Id
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

    @Id
    @Column(name = "create_time")
    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getCode() {
        return code;
    }

    public void setCode(Boolean code) {
        this.code = code;
    }

    @Column//(length = 100)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column(name = "msg_body")
    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    @Column(length = 10)
    public String getPre() {
        return pre;
    }

    public void setPre(String pre) {
        this.pre = pre;
    }

    @Column(name="uuid",insertable=false,updatable=false,length = 100)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @JsonBackReference
    @JoinColumn(name = "uuid")
    @ManyToOne(fetch = FetchType.EAGER)
    public TotalReport getTotalReport() {
        return totalReport;
    }

    public void setTotalReport(TotalReport totalReport) {
        this.totalReport = totalReport;
    }
}
