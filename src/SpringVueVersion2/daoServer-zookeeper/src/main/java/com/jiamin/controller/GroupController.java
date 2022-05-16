package com.jiamin.controller;

import com.jiamin.service.GroupService;
import org.apache.ibatis.annotations.Param;
import org.example.common.entity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GroupController {
    @Autowired
    private GroupService groupService;


    @RequestMapping("/list/group")
    public List<Group> list(String userID) {
        return groupService.selectGroup(userID);
    }

    @RequestMapping("/register/group")
    public boolean register(Group group) {
        return groupService.createGroup(group);
    }

    @RequestMapping("/join")
    public boolean join(String userID, String groupID) {
        return groupService.join(userID, groupID);
    }

    @RequestMapping("/search/group")
    public Group searchGroup(String groupID) {
        return groupService.searchGroup(groupID);
    }
}
