package com.jiamin.controller;

import  org.example.common.entity.servlet.SessionAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;


import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    RestTemplate template;
    @RequestMapping("/login")
    public String login(HttpSession session, @RequestParam("accountID")String accountID, @RequestParam("password")String password){
        accountID = accountID.substring(2);
        //String url = "http://localhost:8080/login";
        //使用注册中心提供的服务，不用提供实际地址和端口
        String url = "http://DAOSERVER/login";
        SessionAttribute sessionAttribute = template.getForObject(url + "?accountID=" + accountID + "&password=" + password, SessionAttribute.class);
        if(sessionAttribute != null){
            session.setAttribute("user", sessionAttribute);
            return "success";
        }
        return "error";
    }

    /*
    *若没有传递账号，密码信息；则查看该用户是否已经登录
    **/
    @RequestMapping(path = "/login", params = {"!accountID","!password"})
    public String success(HttpSession session){
        SessionAttribute user = (SessionAttribute) session.getAttribute("user");
        if(user != null && user.isHaveLogined()) {
            return "success";
        }
        return "login";
    }


}
