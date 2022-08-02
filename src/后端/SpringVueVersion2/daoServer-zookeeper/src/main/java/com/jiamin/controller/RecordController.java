package com.jiamin.controller;

import com.jiamin.service.RecordService;
import org.example.common.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@RestController
public class RecordController {
    @Autowired
    private RecordService recordService;

    @RequestMapping("/sum")
    public int sum(String groupID, String userID, String oppositeID) {
        return recordService.selectRecordSum(groupID, userID, oppositeID);
    }

    @RequestMapping("/list/record")
    public List<Record> record(String groupID, String userID, String oppositeID, long time) {
        return recordService.selectRecord(groupID, userID, oppositeID, time);
    }

}
