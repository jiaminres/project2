package com.jiamin.controller;

import com.jiamin.service.DaoService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.example.common.entity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class GroupController {
    @Autowired
    RestTemplate template;
    @Autowired
    DaoService daoService;


    @RequestMapping("/list/group")
    public List<Group> listOfGroup(String userID) {
        return daoService.listOfGroup(userID);
    }


    @RequestMapping("/register/group")
    public boolean register(Group group) {
        return daoService.register(group);
    }


    @RequestMapping("/join")
    public boolean join(String userID, String groupID) {
        return daoService.join(userID, groupID);
    }

    @RequestMapping("/search/group")
    public Group searchGroup(String groupID) {
        return daoService.searchGroup(groupID);
    }


}
