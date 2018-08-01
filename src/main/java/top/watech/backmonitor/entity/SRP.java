package top.watech.backmonitor.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by fhm on 2018/7/24.
 */
@Entity
@Table(name = "srps")
public class SRP {
    private Long srpId;
    private String srpName;
    private String description;    //描述
    private boolean switchs;//开关
    private double freq;    //频率
    private String remark;  //备注

    private Set<User> users = new HashSet<>();

    private Set<MonitorItem> monitorItems = new HashSet<>();

    @Id
    @Column(name = "srp_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getSrpId() {
        return srpId;
    }

    public void setSrpId(Long srpId) {
        this.srpId = srpId;
    }

    @Column(name = "srp_name")
    public String getSrpName() {
        return srpName;
    }

    public void setSrpName(String srpName) {
        this.srpName = srpName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSwitchs() {
        return switchs;
    }

    public void setSwitchs(boolean switchs) {
        this.switchs = switchs;
    }

    public double getFreq() {
        return freq;
    }

    public void setFreq(double freq) {
        this.freq = freq;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @ManyToMany(mappedBy = "srps",fetch = FetchType.EAGER)
    public Set<User> getUsers() {
        return users;
    } public void setUsers(Set<User> users) {
        this.users = users;
    }

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "srp",cascade={CascadeType.REMOVE})
    public Set<MonitorItem> getMonitorItems() {
        return monitorItems;
    }

    public void setMonitorItems(Set<MonitorItem> monitorItems) {
        this.monitorItems = monitorItems;
    }

    public SRP() {
    }

    public SRP(String srpName) {
        this.srpName = srpName;
    }

    @Override
    public String toString() {
        return "SRP{" +
                "srpId=" + srpId +
                ", srpName='" + srpName + '\'' +
                ", description='" + description + '\'' +
                ", switchs=" + switchs +
                ", freq=" + freq +
                ", remark='" + remark + '\'' +

                '}';
    }
}
