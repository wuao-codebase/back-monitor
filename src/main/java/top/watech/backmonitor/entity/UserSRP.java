package top.watech.backmonitor.entity;

import javax.persistence.*;

/**
 * Created by fhm on 2018/7/24.
 * SRP成员表
 */

@Entity
@Table(name = "user_SRP")
@IdClass(UserSRPMultiKey.class)
public class UserSRP {
    private long userId;
    private Long srpId;

    @Id
    @Column(name = "user_id")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "srp_id")
    public Long getSrpId() {
        return srpId;
    }

    public void setSrpId(Long srpId) {
        this.srpId = srpId;
    }

    @Override
    public String toString() {
        return "UserSRP{" +
                "userId=" + userId +
                ", srpId=" + srpId +
                '}';
    }
}
