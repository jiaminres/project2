package com.jiamin.controller;

import com.jiamin.service.DaoService;
import com.ramostear.captcha.HappyCaptcha;
import com.ramostear.captcha.support.CaptchaStyle;
import com.ramostear.captcha.support.CaptchaType;
import org.example.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

@RestController
public class HappyCaptchaLogin {
    @Autowired
    DaoService daoService;
    //已登录用户的ID集合
    Set<String> loginSet = new HashSet<>();

    @RequestMapping("/login")
    public User login(@RequestParam("userID") String userID, @RequestParam("password") String password) {
        User user = null;
        boolean suc = true;
        //防止同时登录相同客户
        synchronized (this) {
            suc = loginSet.contains(userID);
        }
        if (suc) {
            //已登录用户，ID置为0
            user = new User();
            user.setUserID("0");
        } else {
            user = daoService.login(userID, password);
            if(user != null)
            loginSet.add(user.getUserID());
        }
        return user;
    }

    @RequestMapping("/offLine")
    public void offLine(@RequestParam("userID") String userID) {
        System.out.println(userID);
        synchronized (this) {
            loginSet.remove(userID);
        }
    }

    @RequestMapping("/login/verifyCodeStr")
    public boolean verifyCodeStr(@RequestParam("verifyCodeStr") String verifyCodeStr, HttpServletRequest req) {
        boolean suc = HappyCaptcha.verification(req, verifyCodeStr, true);
        if (suc) return true;
        return false;
    }

    @RequestMapping("/login/verifyCodeImg")
    public void generateImage(HttpServletRequest req, HttpServletResponse res) {
        //允许客户端ajax携带cookie信息
        res.setHeader("Access-Control-Allow-Credentials", "true");
        HappyCaptcha.require(req, res)
                .style(CaptchaStyle.IMG)
                .type(CaptchaType.ARITHMETIC_ZH)
                .build()
                .finish();
    }

}
