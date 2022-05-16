package com.jiamin.controller;

import org.example.common.entity.websocket.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {
    @MessageMapping("/hello")
    @SendTo("/simple/greeting")
    public Message greeting(Message message){
        return message;
    }



}
