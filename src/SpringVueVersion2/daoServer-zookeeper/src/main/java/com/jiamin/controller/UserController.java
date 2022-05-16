package com.jiamin.controller;

import com.jiamin.service.DetailService;
import com.jiamin.service.UserService;
import org.example.common.entity.Detail;
import org.example.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private DetailService detailService;

    //登录用户时，除了获取用户信息，还要获取用户详情信息
    @Transactional
    @RequestMapping("/login")
    public User login( String userID,  String password) {
        User user = userService.selectUserForLogin(userID, password);
        if(user == null){
            throw new RuntimeException("查询用户失败");
        }
        Detail detail = detailService.selectOneDetail(userID);
        if(detail == null){
            throw new RuntimeException("查询用户详情失败");
        }
        user.setDetail(detail);
        return user;
    }

    //注册用户时，需要在user表中插入记录
    //并在detail表也生成一条对应的记录
    @Transactional
    @RequestMapping("/register/user")
    public boolean register(@RequestBody User user) {

        if(!userService.insertOneUser(user)){
            throw new RuntimeException("注册用户失败");
        }
        if(!detailService.insertOneDetail(user.getUserID())){
            throw new RuntimeException("注册详情表失败");
        }
        return true;
    }

    @RequestMapping("/list/user")
    public List<User> list(String userID, String groupID) {
        return userService.selectAllUsersExceptOwn(userID, groupID);
    }

    @RequestMapping("update/userName")
    public boolean modifyUserName(String userID, String userName) {
        return userService.modifyUserName(userID, userName);
    }
    @RequestMapping("update/headImage")
    public boolean modifyHeadImageAddress(String userID, String headImageAddress) {
        return userService.modifyHeadImageAddress(userID, headImageAddress);
    }
}
