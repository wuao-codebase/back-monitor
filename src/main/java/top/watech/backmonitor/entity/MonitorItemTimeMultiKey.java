package top.watech.backmonitor.entity;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * Created by fhm on 2018/7/24.
 */
public class MonitorItemTimeMultiKey implements Serializable{
    private Long monitorId;
    private Date createTime;

    public MonitorItemTimeMultiKey() {
    }

    public MonitorItemTimeMultiKey(Long monitorId, Time createTime) {
        this.monitorId = monitorId;
        this.createTime = createTime;
    }

    public Long getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(Long monitorId) {
        this.monitorId = monitorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    //  ***重写hashCode与equals方法***
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((monitorId == null) ? 0 : monitorId.hashCode());
        result = PRIME * result + ((createTime == null) ? 0 : createTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        if(obj == null){
            return false;
        }
        if(getClass() != obj.getClass()){
            return false;
        }

        final MonitorItemTimeMultiKey other = (MonitorItemTimeMultiKey)obj;
        if(monitorId == null){
            if(other.monitorId != null){
                return false;
            }
        }else if(!monitorId.equals(other.monitorId)){
            return false;
        }
        if(createTime == null){
            if(other.createTime != null){
                return false;
            }
        }else if(!createTime.equals(other.createTime)){
            return false;
        }

        return true;
    }
}
