package top.watech.backmonitor.entity;

import javax.persistence.*;

/**
 * Created by fhm on 2018/7/24.
 */
@Entity
@Table(name = "srps")
public class SRP {
    private Long srpId;
    private String srpName;

    private boolean switchs;//开关
    private double freq;    //频率
    private String desc;    //描述



//    @Id
//    @Column(name = "srp_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    public Long getSrpId() {
//        return srpId;
//    }
//
//    public void setSrpId(Long srpId) {
//        this.srpId = srpId;
//    }
//
//    @Column(name = "srp_name")
//    public String getSrpName() {
//        return srpName;
//    }
//
//    public void setSrpName(String srpName) {
//        this.srpName = srpName;
//    }
//
//    public boolean isSwitchs() {
//        return switchs;
//    }
//
//    public void setSwitchs(boolean switchs) {
//        this.switchs = switchs;
//    }
//
//    public double getFreq() {
//        return freq;
//    }
//
//    public void setFreq(double freq) {
//        this.freq = freq;
//    }
//
////    @Column(length = 1024)
//    public String getDesc() {
//        return desc;
//    }
//
//    public void setDesc(String desc) {
//        this.desc = desc;
//    }
}
