package com.jiamin.service;

import org.example.common.entity.Detail;
import org.example.common.entity.Group;
import org.example.common.entity.Record;
import org.example.common.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.security.util.AuthResources_it;

import java.sql.Timestamp;
import java.util.List;

@Service
public class DaoServiceImp implements DaoService {
    @Override
    public User login(String userID, String password) {
        System.out.println("login发生错误");
        return null;
    }

    @Override
    public boolean register(User user) {
        System.out.println("注册发生错误");
        return false;
    }

    @Override
    public List<User> list(String userID, String groupID) {
        System.out.println("查询用户列表错误");
        return null;
    }

    @Override
    public List<Group> listOfGroup(String userID) {
        System.out.println("查询群组列表失败");
        return null;
    }

    @Override
    public boolean register(Group group) {
        System.out.println("注册群组失败");
        return false;
    }

    @Override
    public boolean join(String userID, String groupID) {
        System.out.println("join失败");
        return false;
    }

    @Override
    public int sum(String groupID, String userID, String oppositeID) {
        System.out.println("sum失败");
        return 0;
    }

    @Override
    public List<Record> record(String groupID, String userID, String oppositeID, long timestamp) {
        System.out.println("record失败");
        return null;
    }

    @Override
    public boolean detailPut(Detail detail) {
        System.out.println("detailPut失败");
        return false;
    }

    @Override
    public Detail detailGet(String userID) {
        System.out.println("detailGet失败");
        return null;
    }

    @Override
    public boolean modifyUserName(String userID, String userName) {
        System.out.println("modifyUserName失败");
        return false;
    }

    @Override
    public boolean modifyHeadImageAddress(String userID, String headImageAddress) {
        System.out.println("modifyHeadImageAddress失败");
        return false;
    }

    @Override
    public Group searchGroup(String groupID) {
        System.out.println("searchGroup失败");
        return null;
    }
}
