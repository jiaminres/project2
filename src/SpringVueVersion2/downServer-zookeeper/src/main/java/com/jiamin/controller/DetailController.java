package com.jiamin.controller;

import com.jiamin.service.DaoService;
import org.example.common.entity.Detail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DetailController {
    @Autowired
    DaoService daoService;

    @RequestMapping("/detail/put")
    //因为在转换为String
    public boolean detailPut(Detail detail) {
        System.out.println(detail);
        return daoService.detailPut(detail);
    }


    @RequestMapping("/detail/get")
    public Detail detailGet(String userID) {
        return daoService.detailGet(userID);
    }
}
