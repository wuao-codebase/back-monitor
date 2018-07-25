package top.watech.backmonitor.entity;

import java.io.Serializable;
import java.sql.Time;

/**
 * Created by fhm on 2018/7/24.
 */
public class SRPTimeMultiKey implements Serializable{
    private Long srpId;
    private Time startTime;

    public Long getSrpId() {
        return srpId;
    }

    public void setSrpId(Long srpId) {
        this.srpId = srpId;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public SRPTimeMultiKey() {
    }

    public SRPTimeMultiKey(Long srpId, Time startTime) {
        this.srpId = srpId;
        this.startTime = startTime;
    }

    //  ***重写hashCode与equals方法***
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((srpId == null) ? 0 : srpId.hashCode());
        result = PRIME * result + ((startTime == null) ? 0 : startTime.hashCode());
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

        final SRPTimeMultiKey other = (SRPTimeMultiKey)obj;
        if(srpId == null){
            if(other.srpId != null){
                return false;
            }
        }else if(!srpId.equals(other.srpId)){
            return false;
        }
        if(startTime == null){
            if(other.startTime != null){
                return false;
            }
        }else if(!startTime.equals(other.startTime)){
            return false;
        }

        return true;
    }
}
