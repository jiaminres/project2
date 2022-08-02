package com.jiamin.controller;

import org.example.common.entity.User;
import org.example.common.entity.websocket.PrivateMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {
    @RequestMapping("/test/obj")
    public User test1(){
        return null;
    }
    @RequestMapping("/test/list")
    List<PrivateMessage> test2(){
        List<PrivateMessage> list = new ArrayList<>();
        list.add(new PrivateMessage());
        return  list;
    }
}
