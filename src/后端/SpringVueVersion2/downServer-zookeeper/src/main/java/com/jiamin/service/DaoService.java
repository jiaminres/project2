package com.jiamin.service;

import org.example.common.entity.Detail;
import org.example.common.entity.Group;
import org.example.common.entity.Record;
import org.example.common.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@Service
@FeignClient(value = "daoServer", fallback = DaoServiceImp.class)
public interface DaoService {
    /*UserService*/
    @RequestMapping("/login")
    public User login(@RequestParam("userID") String userID, @RequestParam("password") String password);

    @RequestMapping("/register/user")
    public boolean register(@RequestBody User user);

    @RequestMapping("/list/user")
    public List<User> list(@RequestParam("userID") String userID, @RequestParam("groupID") String groupID);

    @RequestMapping("update/userName")
    public boolean modifyUserName(@RequestParam("userID") String userID, @RequestParam("userName") String userName);

    @RequestMapping("update/headImage")
    public boolean modifyHeadImageAddress(@RequestParam("userID") String userID, @RequestParam("headImageAddress") String headImageAddress);

    /*GroupService*/
    @RequestMapping("/list/group")
    public List<Group> listOfGroup(@RequestParam("userID") String userID);

    @RequestMapping("/register/group")
    public boolean register(@ModelAttribute Group group);

    @RequestMapping("/join")
    public boolean join(@RequestParam("userID") String userID, @RequestParam("groupID") String groupID);

    @RequestMapping("/search/group")
    public Group searchGroup(@RequestParam("groupID") String groupID);

    /*RecordService*/
    @RequestMapping("/sum")
    public int sum(@RequestParam("groupID") String groupID,
                   @RequestParam("userID") String userID,
                   @RequestParam("oppositeID") String oppositeID);

    @RequestMapping("/list/record")
    public List<Record> record(@RequestParam("groupID") String groupID,
                               @RequestParam("userID") String userID,
                               @RequestParam("oppositeID") String oppositeID,
                               @RequestParam("time") long time);

    /*DetailService*/
    @PostMapping("/detail/put")
    public boolean detailPut(@RequestBody Detail detail);


    @RequestMapping("/detail/get")
    public Detail detailGet(String userID);
}
