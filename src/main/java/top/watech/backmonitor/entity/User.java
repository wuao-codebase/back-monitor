package top.watech.backmonitor.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by fhm on 2018/7/19.
 */
@Entity
@Table(name = "users")
public class User {

    private long userId;
    private String username;
    private String userpwd;

    @JsonIgnore
    private Set<Weibo> weibos;

    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REMOVE},mappedBy = "user")
    public Set<Weibo> getWeibos() {
        return weibos;
    }

    public void setWeibos(Set<Weibo> weibos) {
        this.weibos = weibos;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name="username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "userpwd")
    public String getUserpwd() {
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }
}
