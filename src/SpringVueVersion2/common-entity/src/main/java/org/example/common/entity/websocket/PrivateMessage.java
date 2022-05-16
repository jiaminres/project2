package org.example.common.entity.websocket;

import org.example.common.entity.Record;

import java.io.Serializable;
import java.sql.Timestamp;

public class PrivateMessage implements Serializable {
    private String groupID;
    private String sourceID;
    private String targetID;
    private long time;
    private String message;


    private String sourceName;
    private String targetName;
    private String userNameWithoutId;
    private String headImageAddress;


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getUserNameWithoutId() {
        return userNameWithoutId;
    }

    public void setUserNameWithoutId(String userNameWithoutId) {
        this.userNameWithoutId = userNameWithoutId;
    }

    public String getHeadImageAddress() {
        return headImageAddress;
    }

    public void setHeadImageAddress(String headImageAddress) {
        this.headImageAddress = headImageAddress;
    }

    public Record wrap(){
        Record record = new Record();
        record.setGroupID(this.groupID);
        record.setSourceID(this.sourceID);
        record.setTargetID(this.targetID);
        record.setTime(this.time);
        record.setMessage(this.message);
        return record;
    }

    @Override
    public String toString() {
        return "PrivateMessage{" +
                "groupID='" + groupID + '\'' +
                ", sourceID='" + sourceID + '\'' +
                ", targetID='" + targetID + '\'' +
                ", time=" + time +
                ", message='" + message + '\'' +
                ", sourceName='" + sourceName + '\'' +
                ", targetName='" + targetName + '\'' +
                ", userNameWithoutId='" + userNameWithoutId + '\'' +
                ", headImageAddress='" + headImageAddress + '\'' +
                '}';
    }
}
