//package com.jiamin.controller;
//
//import com.jiamin.service.DaoServerService;
//import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
//import org.example.common.entity.servlet.SessionAttribute;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.client.RestTemplate;
//
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpSession;
//import java.util.Arrays;
//import java.util.concurrent.Executors;
//import java.util.stream.Collectors;
//
////@Controller
///*这样配置的兜底方法适用于全局,方法参数必须为空，返回类型与原方法一致
//* 适用于方法有@HystrixCommand标注，但没指明方法
//* */
//
////@DefaultProperties(defaultFallback = "globalFallback")
//public class LoginController {
//    @Autowired
//    RestTemplate template;
//    @Resource
//    DaoServerService daoServerService;
//
//    /*
//     * Hystrix在超时，进行服务降级时，会给原本的服务线程，发送中断信号，至于是否中断，得看代码是否存在响应中断的逻辑；
//     * 若没有，业务进程仍会继续执行
//     *
//     * 同时另起一条线程，将原本业务函数的参数也一并传送过去,并且声明的方法的参数类型必须与原方法匹配
//     * */
//    /*
//    * 属性可以从 HistrixCommandProperties类中查看
//    * */
//    @HystrixCommand(fallbackMethod = "loginFallback", commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
//            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value="10"),
//            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value="10000"),
//            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value="60")
//    })
//    @RequestMapping("/login")
//    public String login(HttpSession session, @RequestParam("accountID") String accountID, @RequestParam("password") String password) {
//        accountID = accountID.substring(2);
//        //String url = "http://localhost:8080/login";
//        //使用注册中心提供的服务，不用提供实际地址和端口
//
//        /*
//        String url = "http://daoServer/login";
//        SessionAttribute sessionAttribute = template.getForObject(url + "?accountID=" + accountID + "&password=" + password, SessionAttribute.class);
//        */
//
//        //采用openFeign进一步包装，避免手动拼接字符串
//        SessionAttribute sessionAttribute = daoServerService.login(accountID, password);
//        if (sessionAttribute != null) {
//            session.setAttribute("user", sessionAttribute);
//            return "success";
//        }
//        return "error";
//    }
//
//    public String loginFallback(HttpSession session, @RequestParam("accountID") String accountID, @RequestParam("password") String password) {
//        return "error";
//    }
//
//    public String globalFallback() {
//        return "error";
//    }
//
//
//    /*
//     *若没有传递账号，密码信息；则查看该用户是否已经登录
//     **/
//    @RequestMapping(path = "/login", params = {"!accountID", "!password"})
//    public String success(HttpSession session) {
//        SessionAttribute user = (SessionAttribute) session.getAttribute("user");
//        if (user != null && user.isHaveLogined()) {
//            return "success";
//        }
//        return "forward:login";
//    }
//
//
//}
