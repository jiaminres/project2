package org.example.common.entity.servlet;

import org.example.common.entity.User;

import java.io.Serializable;
import java.util.List;

public class SessionAttribute implements Serializable {
    private boolean haveLogined;
    private User user;
    private List<User> userList;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public boolean isHaveLogined() {
        return haveLogined;
    }

    public void setHaveLogined(boolean haveLogined) {
        this.haveLogined = haveLogined;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
