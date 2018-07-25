package top.watech.backmonitor.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

/**
 * Created by fhm on 2018/7/19.
 */
@Entity
@Table(name = "users")
public class User {

    private Long userId;
    private String userName;
    @JsonIgnore
    private String userPwd;

    private String phone;
    private Integer role;   //角色id

    private String nickName; //昵称
    private String email;
    private String orgName; //部门名称
    private String remark;  //备注

    private List<SRP> srps;

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "user_name",nullable = false, length = 20, unique = true)
    @NotEmpty(message = "账号不能为空")
    @Size(min=3, max=20)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "user_pwd",length = 100)
    @NotEmpty(message = "密码不能为空")
    @Size(max=100)
    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    @Column(name = "nick_name")
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "org_name")
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @ManyToMany
    @JoinTable(name = "user_srp",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "srp_id"))
    public List<SRP> getSrps() {
        return srps;
    }

    public void setSrps(List<SRP> srps) {
        this.srps = srps;
    }
}
