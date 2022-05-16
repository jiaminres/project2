package org.example.common.entity;
import java.io.Serializable;

public class Record implements Serializable {
    private String groupID;
    private String sourceID;
    private String targetID;
    private long time;
    private String message;

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public String getTargetID() {
        return targetID;
    }

    public void setTargetID(String targetID) {
        this.targetID = targetID;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    @Override
    public String toString() {
        return "Record{" +
                "groupID='" + groupID + '\'' +
                ", sourceID='" + sourceID + '\'' +
                ", targetID='" + targetID + '\'' +
                ", time=" + time +
                ", message='" + message + '\'' +
                '}';
    }
}
