package com.jiamin.mappers;

import org.apache.ibatis.annotations.Param;
import org.example.common.entity.Record;

import java.sql.Timestamp;
import java.util.List;

public interface RecordMapper {
    //获取用户在某一群组与某一用户的交流记录
    public List<Record> selectRecord(@Param("groupID")String groupID,
                                     @Param("userID")String userID,
                                     @Param("oppositeID") String oppositeID,
                                     @Param("time")long timestamp);
    //获取用户在某一群组与某一用户的交流记录总数
    public int selectRecordSum(@Param("groupID")String groupID,
                               @Param("userID")String userID,
                               @Param("oppositeID")String oppositeID);
    //插入一条记录
    public int insertOneRecord(Record record);

}
