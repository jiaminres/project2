//package com.jiamin.controller;
//
//import org.example.common.entity.websocket.Connect;
//import org.example.common.entity.websocket.Message;
//import org.example.common.entity.websocket.PrivateMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler;
//import org.springframework.stereotype.Controller;
//
//
//
//
//public class GreetingController {
//    @Autowired
//    SimpMessagingTemplate simpMessagingTemplate;
//    @MessageMapping("/hello")
//    @SendTo("/simple/greeting")
//    public Message greeting(Message message){
//        return message;
//    }
//
//    @MessageMapping("/applyConnect")
//    public void applyConnect(Connect connect) {
//        String userName = connect.getTargetUserName();
//        simpMessagingTemplate.convertAndSendToUser(userName, "/simple/applyConnect", connect);
//    }
//    @MessageMapping("/agreeConnect")
//    public void agreeConnect(Connect connect){
//        String userName = connect.getTargetUserName();
//        simpMessagingTemplate.convertAndSendToUser(userName, "/simple/agreeConnect", connect);
//    }
//    @MessageMapping("/sendMessageToUser")
//    public void sendMessageToUser(PrivateMessage privateMessage){
//        String userName = privateMessage.getTarget();
//        simpMessagingTemplate.convertAndSendToUser(userName, "/simple/sendMessageToUser", privateMessage);
//    }
//}
