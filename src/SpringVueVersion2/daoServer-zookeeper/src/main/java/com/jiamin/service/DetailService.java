package com.jiamin.service;

import com.jiamin.mappers.DetailMapper;
import org.example.common.entity.Detail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetailService {
    @Autowired(required = false)
    DetailMapper detailMapper;

    public boolean updateOneDetail(Detail detail) {
        return detailMapper.updateOneDetail(detail);
    }

    public Detail selectOneDetail(String userID) {
        return detailMapper.selectOneDetail(userID);
    }

    public boolean  insertOneDetail(String userID){
        return detailMapper.insertOneDetail(userID);
    }
}
