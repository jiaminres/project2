package com.jiamin.controller;

import com.jiamin.service.DaoService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.example.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.List;


@RestController
public class UserController {
    @Autowired
    RestTemplate template;
    @Autowired
    DaoService daoService;


    @RequestMapping("/register/user")
    public boolean register(User user) {
        return daoService.register(user);
    }


    @RequestMapping("/list/user")
    public List<User> list(HttpSession session, String userID, String groupID) {
        return daoService.list(userID, groupID);
    }

    @RequestMapping("update/userName")
    public boolean modifyUserName(@RequestParam("userID") String userID, @RequestParam("userName") String userName){
        return daoService.modifyUserName(userID,userName);
    }

    @RequestMapping("update/headImage")
    public boolean modifyHeadImageAddress(@RequestParam("userID") String userID, @RequestParam("headImageAddress") String headImageAddress){
        return daoService.modifyHeadImageAddress(userID,headImageAddress);
    }




}
