package com.jiamin.service;

import com.jiamin.mappers.RecordMapper;
import org.apache.ibatis.annotations.Param;
import org.example.common.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class RecordService {
    @Autowired(required = false)
    private RecordMapper recordMapper;


    public List<Record> selectRecord(String groupID, String userID, String oppositeID, long time) {
        return recordMapper.selectRecord(groupID, userID, oppositeID, time);
    }

    public int selectRecordSum(String groupID, String userID, String oppositeID) {
        return recordMapper.selectRecordSum(groupID, userID, oppositeID);
    }

    public int insertOneRecord(Record record) {
        return recordMapper.insertOneRecord(record);
    }


}
