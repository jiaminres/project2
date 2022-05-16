//package com.jiamin.service;
//
//import org.example.common.entity.servlet.SessionAttribute;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//
////@Service
////@FeignClient(value = "daoServer",fallback = DaoServerServiceImp.class)
//public interface DaoServerService {
//    /*以下的函数声明与数据库服务器的函数声明一致，相比只是没有函数体*/
//    @RequestMapping("/login")
//    public SessionAttribute login(@RequestParam("userID")String userID, @RequestParam("password")String password);
//}
