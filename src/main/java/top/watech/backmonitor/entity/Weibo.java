package top.watech.backmonitor.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by fhm on 2018/7/19.
 */
@Entity
@Table(name="weibo")
public class Weibo {

    private long weiboId;
    private User user;
    private String weiboText;
    private Date createDate;
    private Set<Comment> comments;

    public Weibo(){

    }
    public Weibo(User user, String weiboText, Date createDate, Set<Comment> comments) {
        this.user = user;
        this.weiboText = weiboText;
        this.createDate = createDate;
        this.comments = comments;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(long weiboId) {
        this.weiboId = weiboId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "weibo_text")
    public String getWeiboText() {
        return weiboText;
    }

    public void setWeiboText(String weiboText) {
        this.weiboText = weiboText;
    }

    @Column(name = "create_date")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REMOVE},mappedBy = "weibo")
    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
