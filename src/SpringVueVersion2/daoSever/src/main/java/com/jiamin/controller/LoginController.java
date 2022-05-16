package com.jiamin.controller;

import org.example.common.entity.User;
import org.example.common.entity.servlet.SessionAttribute;
import com.jiamin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class LoginController {
//    @Autowired
//    UserService userService;
//    @RequestMapping("/login")
//    @ResponseBody
//    public SessionAttribute login(HttpSession session, @RequestParam("accountID")String accountID, @RequestParam("password")String password){
//            User userFromData = userService.selectUserForLogin(accountID, password);
//            if(userFromData != null && userFromData.getAccountID()!=null){
//                SessionAttribute user = new SessionAttribute();
//                List<User> users = userService.selectAllUsersExceptOwn(accountID);
//                user.setUser(userFromData);
//                user.setHaveLogined(true);
//                user.setUserList(users);
//                return user;
//            }
//              return null;
//        }

}
