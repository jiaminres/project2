package com.jiamin.controller;

import com.jiamin.service.DetailService;
import org.example.common.entity.Detail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

@RestController
public class DetailController {
    @Autowired
    DetailService detailService;
    @RequestMapping("/detail/put")
    public boolean detailPut(@RequestBody  Detail detail){
        System.out.println(detail);
        return detailService.updateOneDetail(detail);
    }

    @RequestMapping("/detail/get")
    public Detail detailGet(String userID){
        return detailService.selectOneDetail(userID);
    }
}
