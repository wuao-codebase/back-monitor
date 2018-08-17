package top.watech.backmonitor.entity;

import lombok.ToString;

import javax.persistence.*;

/**
 * Created by fhm on 2018/8/17.
 */

@Entity
@ToString
@Table(name = "vcm_info")
public class VCMInfo {
    private Integer id;
    private String password;
    private String domain;
    private String apiport;
    private String username;
    private String vmsname;
    private String ivsid;
    private String vcmid;

    @Id
    @Column(length = 30)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(length = 30)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(length = 30)
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Column(length = 30)
    public String getApiport() {
        return apiport;
    }

    public void setApiport(String apiport) {
        this.apiport = apiport;
    }

    @Column(length = 30)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(length = 30)
    public String getVmsname() {
        return vmsname;
    }

    public void setVmsname(String vmsname) {
        this.vmsname = vmsname;
    }

    @Column(length = 30)
    public String getIvsid() {
        return ivsid;
    }

    public void setIvsid(String ivsid) {
        this.ivsid = ivsid;
    }

    @Column(length = 30)
    public String getVcmid() {
        return vcmid;
    }

    public void setVcmid(String vcmid) {
        this.vcmid = vcmid;
    }


}
