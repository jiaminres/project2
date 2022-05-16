package org.example.common.entity;

import java.io.Serializable;

public class UserGroup implements Serializable {
    private String userID;
    private String groupID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    @Override
    public String toString() {
        return "UserGroup{" +
                "userID='" + userID + '\'' +
                ", groupID='" + groupID + '\'' +
                '}';
    }
}
