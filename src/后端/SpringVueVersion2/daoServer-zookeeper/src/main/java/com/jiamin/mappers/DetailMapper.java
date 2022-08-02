package com.jiamin.mappers;

import org.example.common.entity.Detail;

public interface DetailMapper {
    //补充或更新用户详情信息
    public boolean updateOneDetail(Detail detail);
    //查询用户详情信息
    public Detail selectOneDetail(String userID);
    //插入一条详情信息,除了userID,其他皆为null
    public boolean insertOneDetail(String userID);
}
