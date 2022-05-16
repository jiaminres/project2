package com.jiamin.controller;

import org.example.common.entity.websocket.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler;
import org.springframework.stereotype.Controller;



@Controller
public class GreetingController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/hello")
    @SendTo("/simple/greeting")
    public Message greeting(Message message){
        return message;
    }

    @MessageMapping("/applyConnect")
    public void applyConnect(Message message) {
        String userName = message.getUserName();
        System.out.println(userName);
        simpMessagingTemplate.convertAndSendToUser(userName, "/simple/confirmConnect", message);
    }


}
