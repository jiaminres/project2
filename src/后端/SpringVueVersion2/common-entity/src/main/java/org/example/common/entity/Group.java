package org.example.common.entity;

import java.io.Serializable;

public class Group implements Serializable {
    private String groupID;
    private String leaderID;
    private String groupName;
    private String headImageAddress;

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getLeaderID() {
        return leaderID;
    }

    public void setLeaderID(String leaderID) {
        this.leaderID = leaderID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getHeadImageAddress() {
        return headImageAddress;
    }

    public void setHeadImageAddress(String headImageAddress) {
        this.headImageAddress = headImageAddress;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupID='" + groupID + '\'' +
                ", leaderID='" + leaderID + '\'' +
                ", groupName='" + groupName + '\'' +
                ", headImageAddress='" + headImageAddress + '\'' +
                '}';
    }
}
