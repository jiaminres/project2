package org.example.common.entity;

import java.io.Serializable;

public class User implements Serializable {
    private String userID;
    private String password;
    private String userName;
    private String headImageAddress;
    private Detail detail;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadImageAddress() {
        return headImageAddress;
    }

    public void setHeadImageAddress(String headImageAddress) {
        this.headImageAddress = headImageAddress;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", headImageAddress='" + headImageAddress + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  User){
            User user = (User)obj;
            if(user.userID.equals(this.userID)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.userID.hashCode();
    }
}
